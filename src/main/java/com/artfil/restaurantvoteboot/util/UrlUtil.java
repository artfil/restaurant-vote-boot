package com.artfil.restaurantvoteboot.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlUtil {
    public static final String ADMIN_URL = "/rest/admin/users";
    public static final String PROFILE_URL = "/rest/profile";
    public static final String VOTE_URL = "/rest/profile/vote";
    public static final String RESTAURANT_URL = "/rest/restaurants";
    public static final String DISH_URL = "/rest/restaurants/{restId}/dishes";
}
