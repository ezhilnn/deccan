package io.deccan.controlplane.execution.context;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import lombok.Builder;
import lombok.Getter;
import io.deccan.controlplane.execution.context.model.NodeResult;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class ExecutionContext {

        private WorkflowExecution execution;

        private JsonNode input;

        @Builder.Default
        private Map<String,Object> variables =
                new HashMap<>();

        @Builder.Default
        private Map<String,NodeResult> nodeOutputs =
                new HashMap<>();
                public void putVariable(
                String name,
                Object value){

        variables.put(
                name,
                value);

        }

        public Object getVariable(
                String name){

        return variables.get(
                name);

        }

        public boolean hasVariable(
                String name){

        return variables.containsKey(
                name);

        }

        public void removeVariable(
                String name){

        variables.remove(
                name);

        }
        public void putNodeOutput(

                String nodeId,

                NodeResult result){

        nodeOutputs.put(

                nodeId,

                result);

        }

        public NodeResult getNodeOutput(

                String nodeId){

        return nodeOutputs.get(

                nodeId);

        }

        public boolean hasNodeOutput(

                String nodeId){

        return nodeOutputs.containsKey(

                nodeId);

        }

        public void clearNodeOutputs(){

        nodeOutputs.clear();

        }
        public void clearVariables(){

                variables.clear();

        }

}