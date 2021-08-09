package com.overnetcontact.dvvs.service.criteria;

import com.overnetcontact.dvvs.domain.enumeration.NotificationStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.OrgNotification} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.OrgNotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /org-notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrgNotificationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NotificationStatus
     */
    public static class NotificationStatusFilter extends Filter<NotificationStatus> {

        public NotificationStatusFilter() {}

        public NotificationStatusFilter(NotificationStatusFilter filter) {
            super(filter);
        }

        @Override
        public NotificationStatusFilter copy() {
            return new NotificationStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter desc;

    private StringFilter data;

    private BooleanFilter isRead;

    private NotificationStatusFilter status;

    private LongFilter orgUserId;

    public OrgNotificationCriteria() {}

    public OrgNotificationCriteria(OrgNotificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.desc = other.desc == null ? null : other.desc.copy();
        this.data = other.data == null ? null : other.data.copy();
        this.isRead = other.isRead == null ? null : other.isRead.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.orgUserId = other.orgUserId == null ? null : other.orgUserId.copy();
    }

    @Override
    public OrgNotificationCriteria copy() {
        return new OrgNotificationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDesc() {
        return desc;
    }

    public StringFilter desc() {
        if (desc == null) {
            desc = new StringFilter();
        }
        return desc;
    }

    public void setDesc(StringFilter desc) {
        this.desc = desc;
    }

    public StringFilter getData() {
        return data;
    }

    public StringFilter data() {
        if (data == null) {
            data = new StringFilter();
        }
        return data;
    }

    public void setData(StringFilter data) {
        this.data = data;
    }

    public BooleanFilter getIsRead() {
        return isRead;
    }

    public BooleanFilter isRead() {
        if (isRead == null) {
            isRead = new BooleanFilter();
        }
        return isRead;
    }

    public void setIsRead(BooleanFilter isRead) {
        this.isRead = isRead;
    }

    public NotificationStatusFilter getStatus() {
        return status;
    }

    public NotificationStatusFilter status() {
        if (status == null) {
            status = new NotificationStatusFilter();
        }
        return status;
    }

    public void setStatus(NotificationStatusFilter status) {
        this.status = status;
    }

    public LongFilter getOrgUserId() {
        return orgUserId;
    }

    public LongFilter orgUserId() {
        if (orgUserId == null) {
            orgUserId = new LongFilter();
        }
        return orgUserId;
    }

    public void setOrgUserId(LongFilter orgUserId) {
        this.orgUserId = orgUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrgNotificationCriteria that = (OrgNotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(desc, that.desc) &&
            Objects.equals(data, that.data) &&
            Objects.equals(isRead, that.isRead) &&
            Objects.equals(status, that.status) &&
            Objects.equals(orgUserId, that.orgUserId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, desc, data, isRead, status, orgUserId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrgNotificationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (desc != null ? "desc=" + desc + ", " : "") +
            (data != null ? "data=" + data + ", " : "") +
            (isRead != null ? "isRead=" + isRead + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (orgUserId != null ? "orgUserId=" + orgUserId + ", " : "") +
            "}";
    }
}
