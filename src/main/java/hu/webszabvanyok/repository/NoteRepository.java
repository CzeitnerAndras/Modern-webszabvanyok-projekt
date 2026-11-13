package hu.webszabvanyok.repository;

import hu.webszabvanyok.model.Note;
import hu.webszabvanyok.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUserOrderByCreatedAtDesc(User user);
    void deleteByUser(User user);
}
