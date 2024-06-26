/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.tables;


import edu.java.domain.jooq.DefaultSchema;
import edu.java.domain.jooq.Keys;
import edu.java.domain.jooq.tables.records.TgChatsRecord;

import java.time.OffsetDateTime;
import java.util.function.Function;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function2;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class TgChats extends TableImpl<TgChatsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>TG_CHATS</code>
     */
    public static final TgChats TG_CHATS = new TgChats();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<TgChatsRecord> getRecordType() {
        return TgChatsRecord.class;
    }

    /**
     * The column <code>TG_CHATS.ID</code>.
     */
    public final TableField<TgChatsRecord, Long> ID = createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>TG_CHATS.CREATED_AT</code>.
     */
    public final TableField<TgChatsRecord, OffsetDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "");

    private TgChats(Name alias, Table<TgChatsRecord> aliased) {
        this(alias, aliased, null);
    }

    private TgChats(Name alias, Table<TgChatsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>TG_CHATS</code> table reference
     */
    public TgChats(String alias) {
        this(DSL.name(alias), TG_CHATS);
    }

    /**
     * Create an aliased <code>TG_CHATS</code> table reference
     */
    public TgChats(Name alias) {
        this(alias, TG_CHATS);
    }

    /**
     * Create a <code>TG_CHATS</code> table reference
     */
    public TgChats() {
        this(DSL.name("TG_CHATS"), null);
    }

    public <O extends Record> TgChats(Table<O> child, ForeignKey<O, TgChatsRecord> key) {
        super(child, key, TG_CHATS);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public UniqueKey<TgChatsRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_6;
    }

    @Override
    @NotNull
    public TgChats as(String alias) {
        return new TgChats(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public TgChats as(Name alias) {
        return new TgChats(alias, this);
    }

    @Override
    @NotNull
    public TgChats as(Table<?> alias) {
        return new TgChats(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public TgChats rename(String name) {
        return new TgChats(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public TgChats rename(Name name) {
        return new TgChats(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public TgChats rename(Table<?> name) {
        return new TgChats(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row2<Long, OffsetDateTime> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function2<? super Long, ? super OffsetDateTime, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function2<? super Long, ? super OffsetDateTime, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
