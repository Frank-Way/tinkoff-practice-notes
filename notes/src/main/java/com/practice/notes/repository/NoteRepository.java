package com.practice.notes.repository;

import com.practice.notes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>{

    Page<Note> findPageableByUserId(Long userId, Pageable pageable);

//    @Query(value = "select e.* from notes e where e.user_id = :name",
//            nativeQuery = true)
//    List<Note> findNoteByIdUser(@Param("id") Long userId);
}