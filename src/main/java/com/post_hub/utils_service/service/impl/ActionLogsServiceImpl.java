package com.post_hub.utils_service.service.impl;

import com.post_hub.utils_service.entity.ActionLog;
import com.post_hub.utils_service.kafka.model.UtilMessage;
import com.post_hub.utils_service.mapper.ActionLogMapper;
import com.post_hub.utils_service.model.constant.ApiErrorMessage;
import com.post_hub.utils_service.model.dto.ActionLogDTO;
import com.post_hub.utils_service.model.exception.NotFoundException;
import com.post_hub.utils_service.model.request.ActionLogIsReadRequest;
import com.post_hub.utils_service.model.request.ActionLogUpdateResultDTO;
import com.post_hub.utils_service.model.response.PaginationResponse;
import com.post_hub.utils_service.model.response.UtilsResponse;
import com.post_hub.utils_service.repository.ActionLogRepository;
import com.post_hub.utils_service.service.ActionLogService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActionLogsServiceImpl implements ActionLogService {

    private final ActionLogRepository actionLogRepository;
    // достаём данные из базы

    private final ActionLogMapper actionLogMapper;

    @Override
    public UtilsResponse<ActionLogDTO> getActionLogById(Integer logId, Integer userId) {

        ActionLog actionLog;

        if (userId == null) {
            actionLog = actionLogRepository.findById(logId)
                    .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_ACTION_LOG.getMessage(logId)));
        } else {
            actionLog = actionLogRepository.findByIdAndUserId(logId, userId)
                    .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_ACTION_LOG_FOR_USER.getMessage(logId)));
        }
        // убеждаемся, что лог действительно пренадлежит юзеру и выводим ту запись, которая сходится и с logId, и с userId

        return UtilsResponse.createSuccessful(actionLogMapper.map(actionLog));

    }

    @Override
    public UtilsResponse<PaginationResponse<ActionLogDTO>> findAllActionLogs(Pageable pageable) {

        Page<ActionLogDTO> logs = actionLogRepository.findAll(pageable)
                .map(actionLogMapper::map);
        // сначала получаем все данные из базы с учётом переданной пагинации

        PaginationResponse<ActionLogDTO> paginationResponse = new PaginationResponse<>(
                logs.getContent(),
                new PaginationResponse.Pagination(
                        logs.getTotalElements(),
                        pageable.getPageSize(),
                        logs.getNumber() + 1,
                        logs.getTotalPages()
                )
        );
        // создаём ответ, который кладём в список логов и данных о страницах

        return UtilsResponse.createSuccessful(paginationResponse);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    // если в любом месте возникает исключение, то все изменения в базу откатываются

    public UtilsResponse<ActionLogUpdateResultDTO> setIsReadEqualsTrue(@NotNull ActionLogIsReadRequest request) {

        Integer currentUserId = request.getUserId();
        // извлекаем айди из запроса

        List<ActionLog> logs = actionLogRepository.findAllById(request.getIds());
        // собираем все логи по этому айди пользователя

        Map<Boolean, List<Integer>> partitioned = logs.stream()
                .collect(Collectors.partitioningBy(
                        log -> log.getUserId().equals(currentUserId),
                        Collectors.mapping(ActionLog::getId, Collectors.toList())
                ));
        // фильтруем логи, принадлежащие текущему юзеру от всех остальных

        List<Integer> allowedIds = partitioned.get(true);
        // логи, которые принадлежат конкретному юзеру

        List<Integer> skippedIds = partitioned.get(false);
        // логи чужих пользователей, которые пропускаются

        int updatedCount = allowedIds.isEmpty() ? 0 : actionLogRepository.setIsReadEqualsTrue(allowedIds);

        return UtilsResponse.createSuccessful(
                ActionLogUpdateResultDTO.builder()
                        .updatedCount(updatedCount)
                        .updatedIds(allowedIds)
                        .skippedIds(skippedIds)
                        .build()
        );
        // упаковываем результат в DTO
    }

    @Override
    @Transactional
    public ActionLog saveLogFromKafkaMessage(UtilMessage message) {
        ActionLog actionLog = actionLogMapper.mapKafkaMessageToEntity(message);
        return actionLogRepository.save(actionLog);
    }

}
