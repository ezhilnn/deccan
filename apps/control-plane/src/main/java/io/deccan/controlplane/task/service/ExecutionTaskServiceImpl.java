package io.deccan.controlplane.task.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.deccan.controlplane.execution.context.service.ExecutionContextService;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.enums.ExecutionStatus;
import io.deccan.controlplane.execution.repository.WorkflowExecutionRepository;
import io.deccan.controlplane.scheduler.service.WorkflowSchedulerService;
import io.deccan.controlplane.task.dto.response.ExecutionTaskResponse;
import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.task.enums.TaskStatus;
import io.deccan.controlplane.task.factory.ExecutionTaskFactory;
import io.deccan.controlplane.task.mapper.ExecutionTaskMapper;
import io.deccan.controlplane.task.repository.ExecutionTaskRepository;
import io.deccan.controlplane.worker.entity.Worker;
import io.deccan.controlplane.worker.enums.WorkerStatus;
import io.deccan.controlplane.worker.repository.WorkerRepository;
import io.deccan.controlplane.worker.service.WorkerService;
import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ExecutionTaskServiceImpl
        implements ExecutionTaskService {

    private final ExecutionTaskRepository taskRepository;

    private final WorkerRepository workerRepository;

    private final ExecutionTaskFactory taskFactory;

    private final WorkerService workerService;

    private final ObjectMapper objectMapper;

    private final WorkflowExecutionRepository executionRepository;
    private final WorkflowSchedulerService workflowSchedulerService;
    private final ExecutionContextService executionContextService;
    private final ExecutionTaskMapper mapper;

    @Override
    public List<ExecutionTask> createTasks(
            WorkflowExecution execution,
            WorkflowVersion version) {

        try {

            WorkflowDefinition definition =
                    objectMapper.treeToValue(
                            version.getDefinition(),
                            WorkflowDefinition.class);

            List<ExecutionTask> tasks =
                    taskFactory.createTasks(
                            execution,
                            definition);

            return taskRepository.saveAll(tasks);

        } catch (Exception ex) {

            throw new IllegalStateException(
                    "Unable to create execution tasks",
                    ex);

        }

    }

    @Override
    public ExecutionTask leaseTask(
            UUID workerId) {

        Worker worker =
                workerRepository.findById(workerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Worker not found"));

        ExecutionTask task =
                taskRepository
                        .leaseNextTask(
                                TaskStatus.READY,
                                PageRequest.of(0, 1))
                        .stream()
                        .findFirst()
                        .orElse(null);
        
        if (task == null) {
        return null;
        }
        task.setWorker(worker);

        Instant now = Instant.now();

        task.setStatus(
                TaskStatus.LEASED);

        task.setStartedAt(
                now);

        task.setLeasedAt(
                now);

        task.setLeaseUntil(
                now.plusSeconds(60));
        worker.setStatus(
            WorkerStatus.BUSY);
        workerRepository.save(worker);

        return taskRepository.save(task);

    }

    @Override
    public void completeTask(
            UUID taskId) {

        ExecutionTask task =
                taskRepository.findById(taskId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Task not found"));

        task.setStatus(TaskStatus.COMPLETED);

        task.setLeaseUntil(null);

        taskRepository.save(task);

    }

    @Override
    public void failTask(
            UUID taskId,
            String reason) {

       reportFailure(
            taskId,
            reason);

    }
   @Override
        public void reportSuccess(
                UUID taskId,
                JsonNode output){

        ExecutionTask task =
                taskRepository.findById(taskId)
                        .orElseThrow();

        task.setStatus(
                TaskStatus.COMPLETED);

        task.setCompletedAt(
                Instant.now());

        task.setLeaseUntil(null);
        task.setOutput(
        output);


        taskRepository.save(task);

        executionContextService.saveNodeOutput(

                task.getExecution(),

                task.getNodeId(),

                output

        );

        workflowSchedulerService.scheduleNextTasks(
                task);

        if(taskRepository.countByExecutionIdAndStatusNot(

                task.getExecution().getId(),

                TaskStatus.COMPLETED

        ) == 0){

                WorkflowExecution execution =
                        task.getExecution();

                execution.setStatus(
                        ExecutionStatus.COMPLETED);
                 execution.setFinishedAt(
                        OffsetDateTime.now());

                execution.setOutput(
                        output);

                executionRepository.save(
                        execution);

        }

        Worker worker =
                task.getWorker();

        if(worker != null){

                worker.setStatus(
                        WorkerStatus.ONLINE);

                workerRepository.save(
                        worker);

        }

        }
    @Override
    public void reportFailure(
            UUID taskId,
            String errorMessage){

        ExecutionTask task =
                taskRepository.findById(taskId)
                        .orElseThrow();

        // task.setStatus(
        //         TaskStatus.FAILED);

        task.setRetryCount(
            task.getRetryCount()+1);

        task.setErrorMessage(
                errorMessage);

        task.setCompletedAt(
                Instant.now());

        task.setStatus(
                TaskStatus.FAILED);

        task.setLeaseUntil(
                null);


        taskRepository.save(task);

        WorkflowExecution execution =
                task.getExecution();

        execution.setStatus(
                ExecutionStatus.FAILED);
        executionRepository.save(
            execution);
        Worker worker =
        task.getWorker();

        if(worker != null){

            worker.setStatus(
                    WorkerStatus.ONLINE);
            workerRepository.save(worker);

        }

    }
        @Override
        @Transactional(readOnly = true)
        public List<ExecutionTaskResponse> getTasks(
                UUID executionId) {

        return taskRepository.findByExecutionIdWithExecution(executionId)
                .stream()
                .map(mapper::toResponse)
                .toList();

        }
    @Override
        public ExecutionTask leaseNextTask() {

        try {

                Worker worker =
                        workerService.findAvailableWorker();

                return leaseTask(worker.getId());

        } catch (IllegalStateException ex) {

                return null;

        }
        }
        @Override
        public void heartbeat(
                UUID taskId,
                long extendBySeconds){

        ExecutionTask task =
                taskRepository.findById(taskId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Task not found"));

        if(task.getStatus() != TaskStatus.LEASED){

                throw new IllegalStateException(
                        "Task is not currently leased");

        }

        task.setLeaseUntil(

                Instant.now()
                        .plusSeconds(
                                extendBySeconds));

        taskRepository.save(task);

        }
        @Override
        public List<ExecutionTask> recoverExpiredLeases() {

        List<ExecutionTask> expired =

                taskRepository.findByStatusAndLeaseUntilBefore(

                        TaskStatus.LEASED,

                        Instant.now());

        for (ExecutionTask task : expired) {

                Worker worker =
                        task.getWorker();

                if (worker != null) {

                        worker.setStatus(
                                WorkerStatus.ONLINE);

                        workerRepository.save(
                                worker);

                }

                task.setStatus(
                        TaskStatus.READY);

                task.setWorker(null);

                task.setLeaseUntil(null);

                task.setLeasedAt(null);

                task.setRetryCount(
                        task.getRetryCount() + 1);

                }

                return taskRepository.saveAll(expired);

        }
@Override
public void cancelTasks(
        UUID executionId) {

    List<ExecutionTask> tasks =

            taskRepository.findByExecutionIdAndStatusIn(

                    executionId,

                    List.of(

                            TaskStatus.PENDING,

                            TaskStatus.READY,

                            TaskStatus.LEASED

                    ));

    for (ExecutionTask task : tasks) {

        task.setStatus(
                TaskStatus.CANCELLED);

        task.setCompletedAt(
                Instant.now());

        task.setLeaseUntil(null);

        task.setLeasedAt(null);

        Worker worker =
                task.getWorker();

        if (worker != null) {

            worker.setStatus(
                    WorkerStatus.ONLINE);

            workerRepository.save(
                    worker);

        }

    }

    taskRepository.saveAll(tasks);

}
@Override
@Transactional(readOnly = true)
public ExecutionTask getTask(
        UUID taskId){

    return taskRepository.findByIdWithExecution(taskId)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Task not found"));

}
}