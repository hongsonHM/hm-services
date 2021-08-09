package com.overnetcontact.dvvs.service.criteria;

import com.overnetcontact.dvvs.domain.enumeration.Role;
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
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.OrgUser} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.OrgUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /org-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrgUserCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Role
     */
    public static class RoleFilter extends Filter<Role> {

        public RoleFilter() {}

        public RoleFilter(RoleFilter filter) {
            super(filter);
        }

        @Override
        public RoleFilter copy() {
            return new RoleFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter deviceId;

    private StringFilter phone;

    private RoleFilter role;

    private LongFilter internalUserId;

    private LongFilter notificationsId;

    private LongFilter groupId;

    public OrgUserCriteria() {}

    public OrgUserCriteria(OrgUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.deviceId = other.deviceId == null ? null : other.deviceId.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.role = other.role == null ? null : other.role.copy();
        this.internalUserId = other.internalUserId == null ? null : other.internalUserId.copy();
        this.notificationsId = other.notificationsId == null ? null : other.notificationsId.copy();
        this.groupId = other.groupId == null ? null : other.groupId.copy();
    }

    @Override
    public OrgUserCriteria copy() {
        return new OrgUserCriteria(this);
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

    public StringFilter getDeviceId() {
        return deviceId;
    }

    public StringFilter deviceId() {
        if (deviceId == null) {
            deviceId = new StringFilter();
        }
        return deviceId;
    }

    public void setDeviceId(StringFilter deviceId) {
        this.deviceId = deviceId;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public RoleFilter getRole() {
        return role;
    }

    public RoleFilter role() {
        if (role == null) {
            role = new RoleFilter();
        }
        return role;
    }

    public void setRole(RoleFilter role) {
        this.role = role;
    }

    public LongFilter getInternalUserId() {
        return internalUserId;
    }

    public LongFilter internalUserId() {
        if (internalUserId == null) {
            internalUserId = new LongFilter();
        }
        return internalUserId;
    }

    public void setInternalUserId(LongFilter internalUserId) {
        this.internalUserId = internalUserId;
    }

    public LongFilter getNotificationsId() {
        return notificationsId;
    }

    public LongFilter notificationsId() {
        if (notificationsId == null) {
            notificationsId = new LongFilter();
        }
        return notificationsId;
    }

    public void setNotificationsId(LongFilter notificationsId) {
        this.notificationsId = notificationsId;
    }

    public LongFilter getGroupId() {
        return groupId;
    }

    public LongFilter groupId() {
        if (groupId == null) {
            groupId = new LongFilter();
        }
        return groupId;
    }

    public void setGroupId(LongFilter groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrgUserCriteria that = (OrgUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(deviceId, that.deviceId) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(role, that.role) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(notificationsId, that.notificationsId) &&
            Objects.equals(groupId, that.groupId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deviceId, phone, role, internalUserId, notificationsId, groupId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrgUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (deviceId != null ? "deviceId=" + deviceId + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (role != null ? "role=" + role + ", " : "") +
            (internalUserId != null ? "internalUserId=" + internalUserId + ", " : "") +
            (notificationsId != null ? "notificationsId=" + notificationsId + ", " : "") +
            (groupId != null ? "groupId=" + groupId + ", " : "") +
            "}";
    }
}
