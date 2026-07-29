$ErrorActionPreference="Continue"

$baseUrl="http://localhost:8088/api"

$results=@()

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
    Write-Host "========================================"
    Write-Host $Title
    Write-Host "========================================" -ForegroundColor Cyan

    if($Response){
        $Response | ConvertTo-Json -Depth 30
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
        Write-Host "========================================"
        Write-Host "$Name FAILED"
        Write-Host "========================================" -ForegroundColor Red

        if($_.Exception.Response){

            $reader=New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())

            $reader.BaseStream.Position=0

            $reader.DiscardBufferedData()

            $body=$reader.ReadToEnd()

            Write-Host $body -ForegroundColor Red

        }
        else{

            Write-Host $_.Exception.Message -ForegroundColor Red

        }

        Add-Result $Name $false

        return $null

    }

}

Write-Host ""
Write-Host "========================================"
Write-Host "DECCAN MILESTONE 11 VERIFICATION"
Write-Host "========================================"
Write-Host ""

##############################################################
# LOGIN
##############################################################

$login=Invoke-Step "LOGIN" {

    $body=@{
        email="admin@deccan.dev"
        password="change-me"
    }|ConvertTo-Json

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
        name="Milestone11-$([guid]::NewGuid().ToString().Substring(0,8))"
        description="Milestone 11 Verification"
    }|ConvertTo-Json

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

    }|ConvertTo-Json -Depth 20

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
# EXECUTE WORKFLOW
##############################################################

$execution=Invoke-Step "EXECUTE WORKFLOW" {

    $body=@{
        customerId=123
        amount=2500
    }|ConvertTo-Json

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
# REGISTER WORKER
##############################################################

$worker=Invoke-Step "REGISTER WORKER" {

    $body=@{
        workerName="worker-$([guid]::NewGuid().ToString().Substring(0,8))"
        hostName="localhost"
        capabilities=@(
            "http",
            "database",
            "ai"
        )
    }|ConvertTo-Json -Depth 10


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
# HEARTBEAT
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

if(!$lease){
    exit
}

$taskId=$lease.data.id

##############################################################
# LIST EXECUTION TASKS
##############################################################

$list=Invoke-Step "LIST EXECUTION TASKS" {

    Invoke-RestMethod `
    -Method GET `
    -Headers $headers `
    -Uri "$baseUrl/tasks/executions/$executionId"

}

##############################################################
# GET TASK ID FROM LIST IF REQUIRED
##############################################################

if(!$taskId){

    if($list -and $list.data.Count -gt 0){

        $taskId=$list.data[0].id

    }

}

##############################################################
# REPORT TASK SUCCESS
##############################################################

Invoke-Step "REPORT TASK SUCCESS" {

    $body=@{

        success=$true

    }|ConvertTo-Json

    Invoke-RestMethod `
    -Method POST `
    -Headers $headers `
    -Uri "$baseUrl/tasks/$taskId/result" `
    -Body $body

} | Out-Null

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
# GET TASKS AGAIN
##############################################################

Invoke-Step "VERIFY TASK STATUS" {

    Invoke-RestMethod `
    -Method GET `
    -Headers $headers `
    -Uri "$baseUrl/tasks/executions/$executionId"

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
# LIST WORKERS
##############################################################

Invoke-Step "LIST WORKERS" {

    Invoke-RestMethod `
    -Method GET `
    -Headers $headers `
    -Uri "$baseUrl/workers"

} | Out-Null

##############################################################
# SUMMARY
##############################################################

Write-Host ""
Write-Host "========================================"
Write-Host "VERIFICATION SUMMARY"
Write-Host "========================================"

$results | Format-Table -AutoSize

Write-Host ""

$passed=($results | Where-Object {$_.Status -eq "PASS"}).Count
$failed=($results | Where-Object {$_.Status -eq "FAIL"}).Count

Write-Host ""
Write-Host "Passed : $passed" -ForegroundColor Green
Write-Host "Failed : $failed" -ForegroundColor Red

if($failed -eq 0){

    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "MILESTONE 11 VERIFIED SUCCESSFULLY" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green

}
else{

    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "MILESTONE 11 VERIFICATION FAILED" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red

}

##############################################################
# OUTPUT
##############################################################

Write-Host ""
Write-Host "Worker Id    : $workerId"
Write-Host "Workflow Id  : $workflowId"
Write-Host "Execution Id : $executionId"
Write-Host "Task Id      : $taskId"

Write-Host ""
Write-Host "========================================"
Write-Host "END OF MILESTONE 11 VERIFICATION"
Write-Host "========================================"