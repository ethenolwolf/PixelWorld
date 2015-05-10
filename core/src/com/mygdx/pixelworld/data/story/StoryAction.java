package com.mygdx.pixelworld.data.story;

public class StoryAction {
    public final String name;
    public final String param;
    public Story.ActionTypes action;

    public StoryAction(String name, Story.ActionTypes action, String param) {
        this.name = name;
        this.action = action;
        this.param = param;
    }

}
