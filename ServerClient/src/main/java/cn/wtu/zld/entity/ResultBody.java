package cn.wtu.zld.entity;

public class ResultBody {
    private int state;
    private User user;

    public ResultBody(int state, User user) {
        this.state = state;
        this.user = user;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ResultBody{" +
                "state=" + state +
                ", user=" + user +
                '}';
    }
}
