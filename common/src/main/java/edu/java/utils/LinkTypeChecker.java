package edu.java.utils;

import java.net.URI;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkTypeChecker {
    public static final Pattern STACK_OVERFLOW_QUESTION_URL_PATTERN =
        Pattern.compile("^https://stackoverflow.com/questions/(\\d+)$");
    public static final Pattern GIT_HUB_REPO_URL_PATTERN = Pattern.compile("^https://github.com/(\\S+)/(\\S+)$");

    public enum LinkType {
        STACKOVERFLOW_QUESTION,
        GITHUB_REPOSITORY,
        UNKNOWN
    }

    public static LinkType checkLinkType(URI link) {
        if (STACK_OVERFLOW_QUESTION_URL_PATTERN.matcher(link.toString()).find()) {
            return LinkType.STACKOVERFLOW_QUESTION;
        } else if (GIT_HUB_REPO_URL_PATTERN.matcher(link.toString()).find()) {
            return LinkType.GITHUB_REPOSITORY;
        } else {
            return LinkType.UNKNOWN;
        }
    }
}
