package io.deccan.worker.heartbeat;

import io.deccan.worker.service.AuthenticationService;
import io.deccan.worker.dto.request.TaskHeartbeatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import jakarta.annotation.PreDestroy;

import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class TaskHeartbeatServiceImpl
        implements TaskHeartbeatService {

    private final RestClient restClient;

    private final AuthenticationService authenticationService;

    private final ThreadPoolTaskScheduler scheduler =
            new ThreadPoolTaskScheduler();

    private ScheduledFuture<?> future;

    {
        scheduler.initialize();
    }

    @Override
    public void start(
            UUID taskId){

        stop();

        future =
                scheduler.scheduleAtFixedRate(

                        () -> {

                            try{

                                String token =
                                        authenticationService.getToken();

                                TaskHeartbeatRequest request =
                                        new TaskHeartbeatRequest();

                                restClient.post()

                                        .uri("/tasks/{taskId}/heartbeat", taskId)

                                        .header(
                                                HttpHeaders.AUTHORIZATION,
                                                "Bearer " + token)

                                        .body(request)

                                        .retrieve()

                                        .toBodilessEntity();

                            }
                            catch(Exception ignored){

                            }

                        },

                        20000

                );

    }

    @Override
    public void stop(){

        if(future != null){

            future.cancel(true);

            future = null;

        }

    }

    @PreDestroy
    public void destroy(){

        stop();

        scheduler.shutdown();

    }

}