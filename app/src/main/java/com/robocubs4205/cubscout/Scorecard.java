package com.robocubs4205.cubscout;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.apache.commons.lang3.NotImplementedException;

import java.lang.reflect.Type;
import java.util.List;

public class Scorecard {
    @SuppressWarnings("unused")
    public long id;
    public List<ScorecardSection> sections;

    /**
     * Created by trevor on 1/10/17.
     */
    public static class ScorecardFieldSection extends ScorecardSection {
        public Type type;
        public String name;
        public boolean isNullable;

        public ScorecardFieldSection(String name, Type type) {
            this(name, type, false);
        }

        protected ScorecardFieldSection(String name, Type type, boolean isNullable) {
            this.name = name;
            this.type = type;
            this.isNullable = isNullable;
        }

        public enum Type {
            COUNT, RATING
        }
    }

    /**
     * Created by trevor on 1/10/17.
     */
    public static class ScorecardNullableFieldSection extends ScorecardFieldSection {

        public NullWhen nullWhen;
        public String checkBoxMessage;

        public ScorecardNullableFieldSection(String name,
                                             Type type, int index, NullWhen nullWhen,
                                             String checkBoxMessage) {
            super(name, type, true);
            this.nullWhen = nullWhen;
            this.checkBoxMessage = checkBoxMessage;
        }

        public enum NullWhen {
            CHECKED, UNCHECKED
        }
    }

    /**
     * Created by trevor on 1/10/17.
     */
    public static class ScorecardParagraphSection extends ScorecardSection {
        public String paragraph;
    }

    /**
     * Created by trevor on 1/10/17.
     */
    public abstract static class ScorecardSection {
        @SuppressWarnings("unused")
        public long id;
        private long ScorecardId;

        public long getId() {
            return this.id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getScorecardId() {
            return this.ScorecardId;
        }

        public void setScorecardId(long ScorecardId) {
            this.ScorecardId = ScorecardId;
        }
    }

    /**
     * Created by trevor on 1/10/17.
     */
    public static class ScorecardTitleSection extends ScorecardSection {
        public String title;
    }

    /**
     * Created by trevor on 1/11/17.
     */
    public static class ScorecardSectionDeserializer implements JsonDeserializer<ScorecardSection>,
                                                                JsonSerializer<ScorecardSection> {

        @Override
        public ScorecardSection deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            if (object.has("title")) {
                return context.deserialize(json, ScorecardTitleSection.class);
            }
            else if (object.has("paragraph")) {
                return context.deserialize(json, ScorecardParagraphSection.class);
            }
            else if (object.has("is_nullable")) {
                if (object.getAsJsonPrimitive("is_nullable").getAsBoolean()) {
                    return context.deserialize(json, ScorecardNullableFieldSection.class);
                }
                else return context.deserialize(json, ScorecardFieldSection.class);
            }
            else throw new JsonParseException("unknown type of scorecard section");
        }

        @Override
        public JsonElement serialize(ScorecardSection src, Type typeOfSrc,
                                     JsonSerializationContext context) {
            if (src instanceof ScorecardTitleSection) {
                return context.serialize(src, ScorecardTitleSection.class);
            }
            else if (src instanceof ScorecardParagraphSection) {
                return context.serialize(src, ScorecardParagraphSection.class);
            }
            else if (src instanceof ScorecardNullableFieldSection) {
                return context.serialize(src, ScorecardNullableFieldSection.class);
            }
            else if (src instanceof ScorecardFieldSection) {
                return context.serialize(src, ScorecardFieldSection.class);
            }
            else {
                throw new NotImplementedException("unknown type of scorecard section");
            }
        }
    }
}
