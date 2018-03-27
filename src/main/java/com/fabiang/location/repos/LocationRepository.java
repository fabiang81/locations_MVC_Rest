package com.fabiang.location.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.fabiang.location.entities.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
	
	@Query("select type, count(type) from Location group by type")
	public List<Object[]> findTypeAndTypeCount();

}
