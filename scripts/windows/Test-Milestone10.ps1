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
Write-Host "DECCAN MILESTONE 10 VERIFICATION"
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

$organizationId=$me.data.organizationId

##############################################################
# CREATE WORKFLOW
##############################################################

$workflow=Invoke-Step "CREATE WORKFLOW" {

    $body=@{
        organizationId=$organizationId
        name="Milestone10-$([guid]::NewGuid().ToString().Substring(0,8))"
        description="Milestone 10 Verification"
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
##############################################################
# CREATE SCHEDULE
##############################################################

$schedule=Invoke-Step "CREATE SCHEDULE" {

    $body=@{

        type="CRON"

        cronExpression="0 */5 * * * *"

        enabled=$true

    }|ConvertTo-Json

    Invoke-RestMethod `
    -Method POST `
    -Headers $headers `
    -Uri "$baseUrl/workflows/$workflowId/schedules" `
    -Body $body

}

if($schedule){
    $scheduleId=$schedule.data.id
}

##############################################################
# LIST SCHEDULES
##############################################################

Invoke-Step "LIST SCHEDULES" {

    Invoke-RestMethod `
    -Method GET `
    -Headers $headers `
    -Uri "$baseUrl/workflows/$workflowId/schedules"

} | Out-Null

##############################################################
# DISABLE SCHEDULE
##############################################################

if($scheduleId){

Invoke-Step "DISABLE SCHEDULE" {

    Invoke-RestMethod `
    -Method POST `
    -Headers $headers `
    -Uri "$baseUrl/workflows/$workflowId/schedules/$scheduleId/disable"

} | Out-Null

}

##############################################################
# ENABLE SCHEDULE
##############################################################

if($scheduleId){

Invoke-Step "ENABLE SCHEDULE" {

    Invoke-RestMethod `
    -Method POST `
    -Headers $headers `
    -Uri "$baseUrl/workflows/$workflowId/schedules/$scheduleId/enable"

} | Out-Null

}

##############################################################
# REGISTER WEBHOOK
##############################################################

$webhook=Invoke-Step "REGISTER WEBHOOK" {

    Invoke-RestMethod `
    -Method POST `
    -Headers $headers `
    -Uri "$baseUrl/workflows/$workflowId/webhooks"

}

if($webhook){
    $webhookToken=$webhook.data.token
}

##############################################################
# EXECUTE WEBHOOK
##############################################################

if($webhookToken){

Invoke-Step "EXECUTE WEBHOOK" {

    Invoke-RestMethod `
    -Method POST `
    -Uri "$baseUrl/webhooks/$webhookToken" `
    -ContentType "application/json" `
    -Body @'
{
    "customerId":123,
    "amount":1000
}
'@

} | Out-Null

}

##############################################################
# REGISTER KAFKA TRIGGER
##############################################################

$trigger=Invoke-Step "REGISTER KAFKA TRIGGER" {

    $body=@{

        topic="orders.created"

    }|ConvertTo-Json

    Invoke-RestMethod `
    -Method POST `
    -Headers $headers `
    -Uri "$baseUrl/workflows/$workflowId/kafka-triggers" `
    -Body $body

}

if($trigger){
    $triggerId=$trigger.data.id
}
##############################################################
# DISABLE KAFKA TRIGGER
##############################################################

if($triggerId){

Invoke-Step "DISABLE KAFKA TRIGGER" {

    Invoke-RestMethod `
    -Method POST `
    -Headers $headers `
    -Uri "$baseUrl/workflows/$workflowId/kafka-triggers/$triggerId/disable"

} | Out-Null

}

##############################################################
# DELETE KAFKA TRIGGER
##############################################################

if($triggerId){

Invoke-Step "DELETE KAFKA TRIGGER" {

    Invoke-RestMethod `
    -Method DELETE `
    -Headers $headers `
    -Uri "$baseUrl/workflows/$workflowId/kafka-triggers/$triggerId"

} | Out-Null

}

##############################################################
# DELETE SCHEDULE
##############################################################

if($scheduleId){

Invoke-Step "DELETE SCHEDULE" {

    Invoke-RestMethod `
    -Method DELETE `
    -Headers $headers `
    -Uri "$baseUrl/workflows/$workflowId/schedules/$scheduleId"

} | Out-Null

}

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

Write-Host "Passed : $passed" -ForegroundColor Green
Write-Host "Failed : $failed" -ForegroundColor Red

if($failed -eq 0){

    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "MILESTONE 10 VERIFIED SUCCESSFULLY" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green

}
else{

    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "MILESTONE 10 VERIFICATION FAILED" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red

}