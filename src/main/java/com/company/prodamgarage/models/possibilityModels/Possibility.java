package com.company.prodamgarage.models.possibilityModels;

import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.models.user.DefaultUserChanges;
import com.company.prodamgarage.models.user.UserChanges;

import java.util.List;

public class Possibility {
    public String name;
    public String description;
    public UserChanges userChanges = new DefaultUserChanges();
    public List<Event> deferredEvents;
}
