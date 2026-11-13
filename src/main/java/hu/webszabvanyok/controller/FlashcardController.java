package hu.webszabvanyok.controller;

import hu.webszabvanyok.dto.FlashcardDeckDto;
import hu.webszabvanyok.dto.FlashcardDto;
import hu.webszabvanyok.model.User;
import hu.webszabvanyok.repository.UserRepository;
import hu.webszabvanyok.service.FlashcardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

    private final FlashcardService flashcardService;
    private final UserRepository userRepository;

    public FlashcardController(FlashcardService flashcardService, UserRepository userRepository) {
        this.flashcardService = flashcardService;
        this.userRepository = userRepository;
    }

    public record DeckRequest(String name) {
    }

    public record CardRequest(String front, String back) {
    }

    public record ReviewRequest(String rating) {
    }

    @GetMapping("/decks")
    public ResponseEntity<List<FlashcardDeckDto>> listDecks(Principal principal) {
        Optional<User> user = resolve(principal);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(flashcardService.listDecks(user.get()));
    }

    @PostMapping("/decks")
    public ResponseEntity<FlashcardDeckDto> createDeck(@RequestBody DeckRequest request, Principal principal) {
        Optional<User> user = resolve(principal);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(flashcardService.createDeck(user.get(), request == null ? null : request.name()));
    }

    @PutMapping("/decks/{id}")
    public ResponseEntity<FlashcardDeckDto> renameDeck(
            @PathVariable Long id,
            @RequestBody DeckRequest request,
            Principal principal) {
        Optional<User> user = resolve(principal);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(flashcardService.renameDeck(user.get(), id, request == null ? null : request.name()));
    }

    @DeleteMapping("/decks/{id}")
    public ResponseEntity<Void> deleteDeck(@PathVariable Long id, Principal principal) {
        Optional<User> user = resolve(principal);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        flashcardService.deleteDeck(user.get(), id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/decks/{id}/cards")
    public ResponseEntity<List<FlashcardDto>> listCards(@PathVariable Long id, Principal principal) {
        Optional<User> user = resolve(principal);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(flashcardService.listCards(user.get(), id));
    }

    @PostMapping("/decks/{id}/cards")
    public ResponseEntity<FlashcardDto> createCard(
            @PathVariable Long id,
            @RequestBody CardRequest request,
            Principal principal) {
        Optional<User> user = resolve(principal);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        String front = request == null ? null : request.front();
        String back = request == null ? null : request.back();
        return ResponseEntity.ok(flashcardService.createCard(user.get(), id, front, back));
    }

    @PutMapping("/cards/{id}")
    public ResponseEntity<FlashcardDto> updateCard(
            @PathVariable Long id,
            @RequestBody CardRequest request,
            Principal principal) {
        Optional<User> user = resolve(principal);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        String front = request == null ? null : request.front();
        String back = request == null ? null : request.back();
        return ResponseEntity.ok(flashcardService.updateCard(user.get(), id, front, back));
    }

    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id, Principal principal) {
        Optional<User> user = resolve(principal);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        flashcardService.deleteCard(user.get(), id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/due")
    public ResponseEntity<List<FlashcardDto>> dueCards(
            @RequestParam(required = false) Long deckId,
            Principal principal) {
        Optional<User> user = resolve(principal);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(flashcardService.dueCards(user.get(), deckId));
    }

    @PostMapping("/cards/{id}/review")
    public ResponseEntity<FlashcardDto> review(
            @PathVariable Long id,
            @RequestBody ReviewRequest request,
            Principal principal) {
        Optional<User> user = resolve(principal);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(flashcardService.review(user.get(), id, request == null ? null : request.rating()));
    }

    private Optional<User> resolve(Principal principal) {
        if (principal == null) {
            return Optional.empty();
        }
        return userRepository.findByUsername(principal.getName());
    }
}
