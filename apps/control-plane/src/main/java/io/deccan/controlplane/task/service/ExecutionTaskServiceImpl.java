package io.deccan.controlplane.task.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.task.enums.TaskStatus;
import io.deccan.controlplane.task.factory.ExecutionTaskFactory;
import io.deccan.controlplane.task.repository.ExecutionTaskRepository;
import io.deccan.controlplane.worker.entity.Worker;
import io.deccan.controlplane.worker.enums.WorkerStatus;
import io.deccan.controlplane.worker.repository.WorkerRepository;
import io.deccan.controlplane.worker.service.WorkerService;
import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import io.deccan.controlplane.execution.enums.ExecutionStatus;
import io.deccan.controlplane.execution.repository.WorkflowExecutionRepository;

import java.util.UUID;

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
                                TaskStatus.PENDING,
                                PageRequest.of(0, 1))
                        .stream()
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "No pending task available"));

        task.setWorker(worker);

        task.setStatus(TaskStatus.LEASED);
        task.setStartedAt(
        Instant.now());

        task.setLeaseUntil(
                Instant.now().plusSeconds(60));
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

        ExecutionTask task =
                taskRepository.findById(taskId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Task not found"));

        task.setStatus(TaskStatus.FAILED);

        task.setLeaseUntil(null);

        taskRepository.save(task);

    }
    @Override
    public void reportSuccess(
            UUID taskId){

        ExecutionTask task =
                taskRepository.findById(taskId)
                        .orElseThrow();

        task.setStatus(
                TaskStatus.COMPLETED);
        task.setCompletedAt(
        Instant.now());

        task.setLeaseUntil(
                null);


        taskRepository.save(task);

        if(taskRepository.countByExecutionIdAndStatusNot(

                task.getExecution().getId(),

                TaskStatus.COMPLETED

        ) == 0){

            WorkflowExecution execution =
                    task.getExecution();

            execution.setStatus(
                    ExecutionStatus.COMPLETED);
            executionRepository.save(
                execution);

        }
        Worker worker =
        task.getWorker();

        if(worker != null){

            worker.setStatus(
                    WorkerStatus.ONLINE);
            workerRepository.save(worker);

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
    public List<ExecutionTask> getTasks(
            UUID executionId){

        return taskRepository.findByExecutionId(
                executionId);

    }
    @Override
    public ExecutionTask leaseNextTask(){

        Worker worker =
                workerService.findAvailableWorker();

        return leaseTask(
                worker.getId());

    }

}