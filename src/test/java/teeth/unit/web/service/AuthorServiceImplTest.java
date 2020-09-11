package teeth.unit.web.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import teeth.support.exception.DuplicateException;
import teeth.support.exception.NotFoundException;
import teeth.web.entity.Author;
import teeth.web.entity.Book;
import teeth.web.entity.BookAuthor;
import teeth.web.repository.AuthorRepository;
import teeth.web.repository.BookAuthorRepository;
import teeth.web.repository.BookRepository;
import teeth.web.service.AuthorService;
import teeth.web.service.impl.AuthorServiceImpl;

/**
 * <b>Author Service Unit Test</b><br>
 * I suggest to use Mockito
 * 
 * @author Wenbo Wang (jackie-1685@163.com)
 */
@RunWith(SpringRunner.class)
public class AuthorServiceImplTest {

	private @Mock BookRepository bookRepository;
	private @Mock AuthorRepository authorRepository;
	private @Mock BookAuthorRepository bookAuthorRepository;
	private @InjectMocks AuthorService authorService = new AuthorServiceImpl();

	private Long authorId = 1L;
	private Long bookId = 1L;
	private String authorName = "Jackie";
	private String bookName = "Spring Boot Quick Start";

	@Test
	public void testGetAuthorById() {
		Author author = new Author();
		author.setId(authorId);
		author.setAuthorName(authorName);

		Mockito.when(authorRepository.findOne(authorId)).thenReturn(author);

		Author retrievedAuthor = authorService.getAuthor(authorId);

		Assert.assertEquals(author, retrievedAuthor);
	}

	@Test(expected = NotFoundException.class)
	public void testGetNonExistAuthorById() {

		Mockito.when(authorRepository.findOne(authorId)).thenReturn(null);

		authorService.getAuthor(authorId);

	}

	@Test
	public void testCreateAuthorByAuthorName() {

		Author author = new Author();
		author.setAuthorName(authorName);

		Mockito.when(authorRepository.findByAuthorName(authorName)).thenReturn(null);
		Mockito.when(authorRepository.save(author)).thenReturn(author);

		Author savedAuthor = authorService.createAuthor(authorName);

		Assert.assertEquals(author, savedAuthor);
	}

	@Test(expected = DuplicateException.class)
	public void testCreateDuplicateAuthorByAuthorName() {
		Author author = new Author();
		author.setAuthorName(authorName);

		Mockito.when(authorRepository.findByAuthorName(authorName)).thenReturn(author);
		Mockito.when(authorRepository.save(author)).thenReturn(author);

		authorService.createAuthor(authorName);
	}

	@Test
	public void testDeleteAuthorById() {
		Author author = new Author();
		author.setId(authorId);
		author.setAuthorName(authorName);

		Mockito.when(authorRepository.findOne(authorId)).thenReturn(author);

		authorService.deleteAuthor(authorId);

		Mockito.verify(bookAuthorRepository, Mockito.times(1)).deleteByAuthor(author);
		Mockito.verify(authorRepository, Mockito.times(1)).delete(author);
	}

	@Test(expected = NotFoundException.class)
	public void testDeleteNonExistAuthorById() {

		Mockito.when(authorRepository.findOne(authorId)).thenReturn(null);

		authorService.deleteAuthor(authorId);
	}

	@Test
	public void testAssignBook() {
		Author author = new Author();
		author.setId(authorId);
		author.setAuthorName(authorName);

		Book book = new Book();
		book.setId(bookId);
		book.setBookName(bookName);

		BookAuthor bookAuthor = new BookAuthor();
		bookAuthor.setBook(book);
		bookAuthor.setAuthor(author);

		Mockito.when(authorRepository.findOne(authorId)).thenReturn(author);
		Mockito.when(bookRepository.findOne(bookId)).thenReturn(book);
		Mockito.when(bookAuthorRepository.findByBookAndAuthor(book, author)).thenReturn(null);
		Mockito.when(bookAuthorRepository.save(bookAuthor)).thenReturn(bookAuthor);

		Author assignedAuthor = authorService.assignBook(authorId, bookId);

		Assert.assertEquals(author, assignedAuthor);
	}

	@Test(expected = NotFoundException.class)
	public void testAssignBookWithNonExistAuthor() {

		Mockito.when(authorRepository.findOne(authorId)).thenReturn(null);

		authorService.assignBook(authorId, bookId);
	}

	@Test(expected = NotFoundException.class)
	public void testAssignBookWithNonExistBook() {

		Mockito.when(bookRepository.findOne(bookId)).thenReturn(null);

		authorService.assignBook(authorId, bookId);
	}

}
