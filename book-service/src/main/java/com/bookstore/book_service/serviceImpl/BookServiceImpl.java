package com.bookstore.book_service.serviceImpl;

import com.bookstore.book_service.entity.Book;
import com.bookstore.book_service.event.BookStockEvent;
import com.bookstore.book_service.kafka.Producer.BookStockProducer;
import com.bookstore.book_service.repository.BookRepository;
import com.bookstore.book_service.service.BookService;
import com.bookstore.common.dto.BookRequest;
import com.bookstore.common.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookStockProducer bookStockProducer;

    public Book saveBook(Book book){
        bookRepository.save(book);
        return book;
    }

    public String addStock(BookRequest request){
        Optional<Book> optionalBook = bookRepository.findById(request.getBookId());
        if(optionalBook.isPresent()){
            Book book = optionalBook.get();
            book.setStock(book.getStock() + request.getQuantity());
            bookRepository.save(book);
            return "Stock updated";
        }else {
            return "Book not found";
        }
    }

    @Transactional
    public boolean processOrder(Long orderId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found " + bookId));

        boolean available = book.getStock() > 0;

        if (available) {
            book.setStock(book.getStock() - 1);
            bookRepository.save(book);
        }

        BookStockEvent evt = BookStockEvent.builder()
                .orderId(orderId)
                .bookId(bookId)
                .isStockAvailable(available)
                .orderStatus(available ? OrderStatus.CONFIRMED : OrderStatus.REJECTED)
                .eventType(available ? "BOOK_VALIDATED" : "BOOK_VALIDATION_FAILED")
                .build();

        log.info("BookService: Processing orderId={}, bookId={}, available={}", orderId, bookId, available);
        bookStockProducer.sendBookStockEvent(evt);

        return available;
    }






//    @Override
//    public Book createBook(BookRequestDTO dto){
//        Book book = Book.builder()
//                .title(dto.getTitle())
//                .price(dto.getPrice())
//                .stock(dto.getStock())
//                .build();
//        return bookRepository.save(book);
//    }
//
//    @Override
//    public List<Book> getAllBooks(){
//        return bookRepository.findAll();
//    }
//
//    @Override
//    public Book getBookById(Long id){
//        return bookRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Book not found"));
//    }
//
//    @Override
//    @Transactional
//    public boolean processOrder(Long orderId, Long bookId){
//        Book book = bookRepository.findById(bookId).orElse(null);
//        boolean available = false;
//
//        if(book != null && book.getStock() > 0){
//            book.setStock(book.getStock() - 1);
//            bookRepository.save(book);
//            available = true;
//        }
//
//        BookStockEvent event = BookStockEvent.builder()
//                .orderId(orderId)
//                .bookId(bookId)
//                .isStockAvailable(available)
//                .orderStatus(available ? OrderStatus.CONFIRMED : OrderStatus.REJECTED)
//                .eventType(available ? "BOOK_CONFIRMED" : "BOOK_REJECTED")
//                .build();
//        log.info("processing book stock for orderId: {} - Available: {}", orderId,available);
//        bookStockProducer.sendBookStockEvent(event);
//        return available;
//    }
}
