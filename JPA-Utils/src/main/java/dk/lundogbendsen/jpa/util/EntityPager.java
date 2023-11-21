package dk.lundogbendsen.jpa.util;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class EntityPager<T> {

	private int pageSize;
	private int currentPage = -1;
	private int pageCount;
	private int entityCount;
	private Class<T> entityType;
	private String countQuery;
	private String selectQuery;

	public EntityPager(EntityManager entityManager, Class<T> entityType, int pageSize, String countQuery,
			String selectQuery) {
		this.pageSize = pageSize;
		this.entityType = entityType;
		this.countQuery = countQuery;
		this.selectQuery = selectQuery;
		this.refreshPageAndEntityCount(entityManager);
	}

	public EntityPager(EntityManager entityManager, Class<T> entityType, int pageSize) {
		this.pageSize = pageSize;
		this.entityType = entityType;
		this.countQuery = "select count(e) from " + entityType.getSimpleName() + " e";
		this.selectQuery = "select e from " + entityType.getSimpleName() + " e";
		this.refreshPageAndEntityCount(entityManager);
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getEntityCount() {
		return entityCount;
	}

	public int getCurrentPageNumber() {
		return currentPage;
	}

	public void refreshPageAndEntityCount(EntityManager entityManager) {
		Query query = entityManager.createQuery(countQuery);
		this.entityCount = ((Long) query.getSingleResult()).intValue();
		double pageCountAsDouble = ((double) this.entityCount) / pageSize;
		this.pageCount = (int) Math.ceil(pageCountAsDouble);
	}

	@SuppressWarnings("unchecked")
	public List<T> getPage(EntityManager entityManager, int pageNo) {
		this.currentPage = pageNo;
		Query query = entityManager.createQuery(selectQuery);
		query.setFirstResult(this.currentPage * this.pageSize);
		query.setMaxResults(this.pageSize);
		return (List<T>) query.getResultList();
	}

	public boolean hasPreviousPage() {
		return currentPage > 0;
	}

	public List<T> previousPage(EntityManager entityManager) {
		this.currentPage--;
		return getPage(entityManager, this.currentPage);
	}

	public List<T> firstPage(EntityManager entityManager) {
		return getPage(entityManager, 0);
	}

	public boolean hasNextPage() {
		return currentPage < (pageCount - 1);
	}

	public List<T> nextPage(EntityManager entityManager) {
		this.currentPage++;
		return getPage(entityManager, this.currentPage);
	}

	public List<T> lastPage(EntityManager entityManager) {
		return getPage(entityManager, pageCount - 1);
	}

	@Override
	public String toString() {
		String s = "EntityPager[";
		s += "currentPage=" + this.currentPage;
		s += ",pageCount=" + this.pageCount;
		s += ",hasNextPage=" + this.hasNextPage();
		s += ",hasPreviousPage=" + this.hasPreviousPage();
		s += ",pageSize=" + this.pageSize;
		s += ",entityCount=" + this.entityCount;
		s += ",entityType=" + this.entityType.getCanonicalName();
		s += ",countQuery=[" + this.countQuery + "]";
		s += ",selectQuery=[" + this.selectQuery + "]]";
		return s;
	}
}
