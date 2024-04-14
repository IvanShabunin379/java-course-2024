package edu.java.utils;

import lombok.experimental.UtilityClass;
import java.net.URI;
import java.util.regex.Pattern;

@UtilityClass
public class LinkTypeChecker {
        public static final Pattern STACK_OVERFLOW_QUESTION_URL_PATTERN =
            Pattern.compile("https://stackoverflow.com/questions/(\\d+)");
        public static final Pattern GIT_HUB_REPO_URL_PATTERN = Pattern.compile("https://github.com/(\\w+)/(\\w+)");

        public enum LinkType {
            STACKOVERFLOW_QUESTION,
            GITHUB_REPOSITORY,
            UNSUPPORTED
        }

        public static LinkType checkLinkType(URI link) {
            if (STACK_OVERFLOW_QUESTION_URL_PATTERN.matcher(link.toString()).find()) {
                return LinkType.STACKOVERFLOW_QUESTION;
            } else if (GIT_HUB_REPO_URL_PATTERN.matcher(link.toString()).find()) {
                return LinkType.GITHUB_REPOSITORY;
            } else {
                return LinkType.UNSUPPORTED;
            }
        }
    }
