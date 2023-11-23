package lundogbendsen.model;

import java.lang.reflect.Field;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class EntityEventLogger {
	@PrePersist
	public void onPrePersist(Object entity) {
		Integer id = extractIdFieldFromEntity(entity);
		System.out.println("--==<[ EntityEventLogger.onPrePersist(id=" + id + ") ]>==--");
	}


	@PostPersist
	public void onPostPersist(Object entity) {
		Integer id = extractIdFieldFromEntity(entity);
		System.out.println("--==<[ EntityEventLogger.onPostPersist(id=" + id + ") ]>==--");
	}

	@PreRemove
	public void onPreRemove(Object entity) {
		Integer id = extractIdFieldFromEntity(entity);
		System.out.println("--==<[ EntityEventLogger.onPreRemove(id=" + id + ") ]>==--");
	}

	@PostRemove
	public void onPostRemove(Object entity) {
		Integer id = extractIdFieldFromEntity(entity);
		System.out.println("--==<[ EntityEventLogger.onPostRemove(id=" + id + ") ]>==--");
	}

	@PreUpdate
	public void onPreUpdate(Object entity) {
		Integer id = extractIdFieldFromEntity(entity);
		System.out.println("--==<[ EntityEventLogger.onPreUpdate(id=" + id + ") ]>==--");
	}

	@PostUpdate
	public void onPostUpdate(Object entity) {
		Integer id = extractIdFieldFromEntity(entity);
		System.out.println("--==<[ EntityEventLogger.onPostUpdate(id=" + id + ") ]>==--");
	}

	@PostLoad
	public void onPostLoad(Object entity) {
		Integer id = extractIdFieldFromEntity(entity);
		System.out.println("--==<[ EntityEventLogger.onPostLoad(id=" + id + ") ]>==--");
	}
	
	private Integer extractIdFieldFromEntity(Object entity) {
		Integer id = null;
		try {
			Field field = entity.getClass().getDeclaredField("id");
			field.setAccessible(true);
			Object fieldValue = field.get(entity);
			id = (Integer) fieldValue;
		} catch (Exception e) {
			// Ignore
		}
		return id;
	}

}
