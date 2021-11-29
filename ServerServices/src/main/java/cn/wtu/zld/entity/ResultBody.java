package cn.wtu.zld.entity;

public class ResultBody {
    private int state;
    private Object object;

    public ResultBody(int state, Object object) {
        this.state = state;
        this.object = object;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "ResultBody{" +
                "state=" + state +
                ", object=" + object +
                '}';
    }
}
