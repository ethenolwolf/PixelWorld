package com.mygdx.pixelworld.data.story;

public class StoryAction {
    public String name;
    public Story.ActionTypes action;
    public String param;

    public StoryAction(String name, Story.ActionTypes action, String param) {
        this.name = name;
        this.action = action;
        this.param = param;
    }

}
