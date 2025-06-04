package org.example.note.adapter.out.repository;

import org.example.note.domain.entity.Note;
import org.example.note.domain.entity.NoteTag;
import org.example.note.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteTagJpaRepository extends JpaRepository<NoteTag, Long> {

    void deleteByNoteAndTag(Note note, Tag tag);

}
