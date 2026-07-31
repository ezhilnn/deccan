$ErrorActionPreference="Continue"

$baseUrl="http://localhost:8088/api"

$results=@()

$workflowId=$null
$executionId=$null
$retryExecutionId=$null
$workerId=$null
$taskId=$null
$organizationId=$null
$importWorkflowId=$null

##############################################################
# COMMON FUNCTIONS
##############################################################

function Add-Result{

    param(

        [string]$Step,

        [bool]$Success

    )

    $script:results += [PSCustomObject]@{

        Step=$Step

        Status=if($Success){"PASS"}else{"FAIL"}

    }

}

function Show-Response{

    param(

        [string]$Title,

        $Response

    )

    Write-Host ""

    Write-Host "========================================================"

    Write-Host $Title -ForegroundColor Cyan

    Write-Host "========================================================"

    if($Response){

        $Response | ConvertTo-Json -Depth 40

    }

    Write-Host ""

}

function Invoke-Step{

    param(

        [string]$Name,

        [scriptblock]$Action

    )

    try{

        $response=& $Action

        Show-Response $Name $response

        Add-Result $Name $true

        return $response

    }

    catch{

        Write-Host ""

        Write-Host "========================================================"

        Write-Host "$Name FAILED" -ForegroundColor Red

        Write-Host "========================================================"

        if($_.Exception.Response){

            $reader=New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())

            $reader.BaseStream.Position=0

            $reader.DiscardBufferedData()

            Write-Host ($reader.ReadToEnd()) -ForegroundColor Red

        }
        else{

            Write-Host $_.Exception.Message -ForegroundColor Red

        }

        Add-Result $Name $false

        return $null

    }

}

Write-Host ""

Write-Host "========================================================" -ForegroundColor Green

Write-Host "DECCAN COMPLETE CONTROL PLANE VERIFICATION"

Write-Host "========================================================" -ForegroundColor Green

Write-Host ""

##############################################################
# LOGIN
##############################################################

$login=Invoke-Step "LOGIN" {

    $body=@{

        email="admin@deccan.dev"

        password="change-me"

    } | ConvertTo-Json

    Invoke-RestMethod `
        -Method POST `
        -Uri "$baseUrl/auth/login" `
        -ContentType "application/json" `
        -Body $body

}

if(!$login){

    exit

}

$headers=@{

    Authorization="Bearer $($login.data.accessToken)"

    "Content-Type"="application/json"

}

##############################################################
# WHO AM I
##############################################################

$me=Invoke-Step "WHO AM I" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/me"

}

if(!$me){

    exit

}

$organizationId=$me.data.organizationId

##############################################################
# CREATE WORKFLOW
##############################################################

$workflow=Invoke-Step "CREATE WORKFLOW" {

    $body=@{

        organizationId=$organizationId

        name="Verify-$([Guid]::NewGuid().ToString().Substring(0,8))"

        description="Complete Verification"

    } | ConvertTo-Json

    Invoke-RestMethod `
        -Method POST `
        -Headers $headers `
        -Uri "$baseUrl/workflows" `
        -Body $body

}

if(!$workflow){

    exit

}

$workflowId=$workflow.data.id

##############################################################
# GET WORKFLOW
##############################################################

Invoke-Step "GET WORKFLOW" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/workflows/$workflowId"

} | Out-Null

##############################################################
# UPDATE WORKFLOW
##############################################################

$updatedName="Updated-$([Guid]::NewGuid().ToString().Substring(0,6))"

Invoke-Step "UPDATE WORKFLOW" {

    $body=@{

        name=$updatedName

        description="Updated Through Verification"

    } | ConvertTo-Json

    Invoke-RestMethod `
        -Method PUT `
        -Headers $headers `
        -Uri "$baseUrl/workflows/$workflowId" `
        -Body $body

} | Out-Null

##############################################################
# GET WORKFLOW AGAIN
##############################################################

Invoke-Step "VERIFY UPDATED WORKFLOW" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/workflows/$workflowId"

} | Out-Null

##############################################################
# LIST WORKFLOWS
##############################################################

Invoke-Step "LIST WORKFLOWS" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/workflows/organizations/$organizationId?page=0&size=20"

} | Out-Null

##############################################################
# PUBLISH WORKFLOW
##############################################################

$publish=Invoke-Step "PUBLISH WORKFLOW" {

    $body=@{

        definition=@{

            schemaVersion=1

            trigger=@{

                type="manual"

            }

            variables=@()

            nodes=@(

                @{

                    id="start"

                    type="manual-trigger"

                    name="Start"

                    configuration=@{}

                },

                @{

                    id="http"

                    type="http"

                    name="HTTP"

                    configuration=@{

                        method="GET"

                        url="https://jsonplaceholder.typicode.com/todos/1"

                    }

                },

                @{

                    id="response"

                    type="response"

                    name="Response"

                    configuration=@{}

                }

            )

            edges=@(

                @{

                    source="start"

                    target="http"

                },

                @{

                    source="http"

                    target="response"

                }

            )

        }

    } | ConvertTo-Json -Depth 30

    Invoke-RestMethod `
        -Method POST `
        -Headers $headers `
        -Uri "$baseUrl/workflows/$workflowId/publish" `
        -Body $body

}

if(!$publish){

    exit

}
##############################################################
# GET WORKFLOW VERSIONS
##############################################################

Invoke-Step "GET WORKFLOW VERSIONS" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/workflows/$workflowId/versions"

} | Out-Null

##############################################################
# EXECUTE WORKFLOW
##############################################################

$execution=Invoke-Step "EXECUTE WORKFLOW" {

    $body=@{

        input=@{

            customerId=12345

            customerName="John Doe"

            amount=2500

            currency="INR"

        }

    } | ConvertTo-Json -Depth 20

    Invoke-RestMethod `
        -Method POST `
        -Headers $headers `
        -Uri "$baseUrl/executions/workflows/$workflowId" `
        -Body $body

}

if(!$execution){

    exit

}

$executionId=$execution.data.id

##############################################################
# GET EXECUTION
##############################################################

Invoke-Step "GET EXECUTION" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/executions/$executionId"

} | Out-Null

##############################################################
# LIST EXECUTIONS
##############################################################

Invoke-Step "LIST EXECUTIONS" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/executions/workflows/$workflowId"

} | Out-Null

##############################################################
# REGISTER WORKER
##############################################################

$worker=Invoke-Step "REGISTER WORKER" {

    $body=@{

        workerName="worker-$([Guid]::NewGuid().ToString().Substring(0,8))"

        hostName="localhost"

        capabilities=@(

            "http"

            "database"

            "kafka"

            "email"

            "ai"

        )

    } | ConvertTo-Json -Depth 20

    Invoke-RestMethod `
        -Method POST `
        -Headers $headers `
        -Uri "$baseUrl/workers" `
        -Body $body

}

if(!$worker){

    exit

}

$workerId=$worker.data.id

##############################################################
# GET WORKER
##############################################################

Invoke-Step "GET WORKER" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/workers/$workerId"

} | Out-Null

##############################################################
# LIST WORKERS
##############################################################

Invoke-Step "LIST WORKERS" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/workers"

} | Out-Null

##############################################################
# WORKER HEARTBEAT
##############################################################

Invoke-Step "WORKER HEARTBEAT" {

    Invoke-RestMethod `
        -Method POST `
        -Headers $headers `
        -Uri "$baseUrl/workers/$workerId/heartbeat"

} | Out-Null

##############################################################
# LEASE TASK
##############################################################

$lease=Invoke-Step "LEASE TASK" {

    Invoke-RestMethod `
        -Method POST `
        -Headers $headers `
        -Uri "$baseUrl/tasks/lease"

}

if($lease){

    $taskId=$lease.data.id

}

##############################################################
# LIST EXECUTION TASKS
##############################################################

$listTasks=Invoke-Step "LIST EXECUTION TASKS" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/tasks/executions/$executionId"

}

if(!$taskId){

    if($listTasks){

        if($listTasks.data.Count -gt 0){

            $taskId=$listTasks.data[0].id

        }

    }

}

if(!$taskId){

    exit

}
##############################################################
# REPORT TASK SUCCESS
##############################################################

Invoke-Step "REPORT TASK SUCCESS" {

    $body=@{

        success=$true

    } | ConvertTo-Json

    Invoke-RestMethod `
        -Method POST `
        -Headers $headers `
        -Uri "$baseUrl/tasks/$taskId/result" `
        -Body $body

} | Out-Null

##############################################################
# VERIFY TASK STATUS
##############################################################

Invoke-Step "VERIFY TASK STATUS" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/tasks/executions/$executionId"

} | Out-Null

##############################################################
# VERIFY EXECUTION AFTER TASK COMPLETION
##############################################################

Invoke-Step "VERIFY EXECUTION STATUS" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/executions/$executionId"

} | Out-Null

##############################################################
# RETRY EXECUTION
##############################################################

$retryExecution=Invoke-Step "RETRY EXECUTION" {

    Invoke-RestMethod `
        -Method POST `
        -Headers $headers `
        -Uri "$baseUrl/executions/$executionId/retry"

}

if($retryExecution){

    $retryExecutionId=$retryExecution.data.id

}

##############################################################
# VERIFY RETRIED EXECUTION
##############################################################

if($retryExecutionId){

Invoke-Step "GET RETRIED EXECUTION" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/executions/$retryExecutionId"

} | Out-Null

}

##############################################################
# CANCEL RETRIED EXECUTION
##############################################################

if($retryExecutionId){

Invoke-Step "CANCEL RETRIED EXECUTION" {

    Invoke-RestMethod `
        -Method POST `
        -Headers $headers `
        -Uri "$baseUrl/executions/$retryExecutionId/cancel"

} | Out-Null

}

##############################################################
# VERIFY CANCELLED EXECUTION
##############################################################

if($retryExecutionId){

Invoke-Step "VERIFY CANCELLED EXECUTION" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/executions/$retryExecutionId"

} | Out-Null

}

##############################################################
# EXPORT WORKFLOW
##############################################################

$export=Invoke-Step "EXPORT WORKFLOW" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/workflows/$workflowId/export/1"

}

##############################################################
# IMPORT WORKFLOW
##############################################################

if($export){

$import=Invoke-Step "IMPORT WORKFLOW" {

    $body=$export.data | ConvertTo-Json -Depth 50

    Invoke-RestMethod `
        -Method POST `
        -Headers $headers `
        -Uri "$baseUrl/workflows/import/$organizationId" `
        -Body $body

}

if($import){

    $importWorkflowId=$import.data.id

}

}
##############################################################
# GET IMPORTED WORKFLOW
##############################################################

if($importWorkflowId){

Invoke-Step "GET IMPORTED WORKFLOW" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/workflows/$importWorkflowId"

} | Out-Null

}

##############################################################
# ARCHIVE IMPORTED WORKFLOW
##############################################################

if($importWorkflowId){

Invoke-Step "ARCHIVE IMPORTED WORKFLOW" {

    Invoke-RestMethod `
        -Method POST `
        -Headers $headers `
        -Uri "$baseUrl/workflows/$importWorkflowId/archive"

} | Out-Null

}

##############################################################
# VERIFY ARCHIVED WORKFLOW
##############################################################

if($importWorkflowId){

Invoke-Step "VERIFY ARCHIVED WORKFLOW" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/workflows/$importWorkflowId"

} | Out-Null

}

##############################################################
# ACTIVATE IMPORTED WORKFLOW
##############################################################

if($importWorkflowId){

Invoke-Step "ACTIVATE IMPORTED WORKFLOW" {

    Invoke-RestMethod `
        -Method POST `
        -Headers $headers `
        -Uri "$baseUrl/workflows/$importWorkflowId/activate"

} | Out-Null

}

##############################################################
# VERIFY ACTIVATED WORKFLOW
##############################################################

if($importWorkflowId){

Invoke-Step "VERIFY ACTIVATED WORKFLOW" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/workflows/$importWorkflowId"

} | Out-Null

}

##############################################################
# DELETE IMPORTED WORKFLOW
##############################################################

if($importWorkflowId){

Invoke-Step "DELETE IMPORTED WORKFLOW" {

    Invoke-RestMethod `
        -Method DELETE `
        -Headers $headers `
        -Uri "$baseUrl/workflows/$importWorkflowId"

} | Out-Null

}

##############################################################
# VERIFY WORKFLOW LIST
##############################################################

Invoke-Step "VERIFY WORKFLOW LIST" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/workflows/organizations/$organizationId?page=0&size=20"

} | Out-Null

##############################################################
# VERIFY WORKER
##############################################################

Invoke-Step "VERIFY WORKER" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/workers/$workerId"

} | Out-Null

##############################################################
# VERIFY EXECUTION
##############################################################

Invoke-Step "FINAL EXECUTION VERIFICATION" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/executions/$executionId"

} | Out-Null

##############################################################
# VERIFY TASKS
##############################################################

Invoke-Step "FINAL TASK VERIFICATION" {

    Invoke-RestMethod `
        -Method GET `
        -Headers $headers `
        -Uri "$baseUrl/tasks/executions/$executionId"

} | Out-Null
##############################################################
# VERIFICATION SUMMARY
##############################################################

Write-Host ""

Write-Host "========================================================" -ForegroundColor Cyan

Write-Host "VERIFICATION SUMMARY"

Write-Host "========================================================" -ForegroundColor Cyan

$results | Format-Table -AutoSize

Write-Host ""

$passed=($results | Where-Object {$_.Status -eq "PASS"}).Count

$failed=($results | Where-Object {$_.Status -eq "FAIL"}).Count

Write-Host "Passed : $passed" -ForegroundColor Green

Write-Host "Failed : $failed" -ForegroundColor Red

Write-Host ""

##############################################################
# GENERATED IDS
##############################################################

Write-Host "========================================================" -ForegroundColor Yellow

Write-Host "GENERATED RESOURCE IDS"

Write-Host "========================================================" -ForegroundColor Yellow

Write-Host ""

Write-Host "Organization Id : $organizationId"

Write-Host "Workflow Id     : $workflowId"

Write-Host "Execution Id    : $executionId"

Write-Host "Retry Execution : $retryExecutionId"

Write-Host "Worker Id       : $workerId"

Write-Host "Task Id         : $taskId"

Write-Host "Imported WF Id  : $importWorkflowId"

Write-Host ""

##############################################################
# FINAL RESULT
##############################################################

if($failed -eq 0){

    Write-Host ""

    Write-Host "########################################################" -ForegroundColor Green

    Write-Host "#                                                      #" -ForegroundColor Green

    Write-Host "#      ALL CONTROL PLANE TESTS PASSED SUCCESSFULLY      #" -ForegroundColor Green

    Write-Host "#                                                      #" -ForegroundColor Green

    Write-Host "########################################################" -ForegroundColor Green

}
else{

    Write-Host ""

    Write-Host "########################################################" -ForegroundColor Red

    Write-Host "#                                                      #" -ForegroundColor Red

    Write-Host "#      ONE OR MORE TESTS FAILED                         #" -ForegroundColor Red

    Write-Host "#                                                      #" -ForegroundColor Red

    Write-Host "########################################################" -ForegroundColor Red

}

Write-Host ""

Write-Host "========================================================"

Write-Host "END OF CONTROL PLANE VERIFICATION"

Write-Host "========================================================"

Write-Host ""