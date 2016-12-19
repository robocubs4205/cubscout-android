package com.robocubs4205.cubscout;

import java.util.List;

/**
 * Created by trevor on 12/14/16.
 */
public class Scorecard {
    public int id;
    public String type;
    public String name;
    public int year;
    List<ScorecardSection> sections;
    public static abstract class ScorecardSection
    {
        public int id;
        public int index;
    }

    public static class ScorecardFieldSection extends ScorecardSection
    {
        public enum Type
        {
            COUNT,RATING
        }
        public Type type;
        public String name;
        public boolean isNullable;
        public int value;
    }

    public static class ScorecardNullableFieldSection extends ScorecardFieldSection {
        public enum NullWhen
        {
            CHECKED,UNCHECKED
        }
        public NullWhen nullWhen;
        public String checkBoxMessage;
        public boolean checked;
    }

    public static class ScorecardTitleSection extends ScorecardSection
    {
        String title;
    }

    public static class ScorecardParagraphSection extends ScorecardSection
    {
        String paragraph;
    }
}
