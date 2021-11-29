package cn.wtu.zld.entity;

import java.util.Arrays;

public class Message {
    private int length;
    private String methodName;
    private Object[] parameters;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Message(int length, String methodName, Object[] parameters) {
        this.length = length;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "Message{" +
                "length=" + length +
                ", methodName='" + methodName + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
