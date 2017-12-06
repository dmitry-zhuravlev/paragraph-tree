package demo.paragraphtree.service

import demo.paragraphtree.model.Paragraph
import demo.paragraphtree.repository.ParagraphRepository
import org.hamcrest.Matchers.emptyIterable
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

/**
 * @author Dmitry Zhuravlev
 * Date:  04.12.2017
 */
@RunWith(SpringRunner::class)
@IntegrationTest
@Transactional
class ParagraphOperationsServiceTest {

    companion object {
        const val DOCUMENT_ID = 1L
        const val P1_TEXT = "p1 text"
        const val P2_TEXT = "p2 text"
        const val MIDDLE_PARAGRAPH_TEXT = "middle paragraph text"
        const val NEW_FIRST_PARAGRAPH_TEXT = "new first paragraph text"
        const val NEW_LAST_PARAGRAPH_TEXT = "new last paragraph text"
        const val CHILD_PARAGRAPH_TEXT = "child paragraph text"
        const val SECOND_CHILD_PARAGRAPH_TEXT = "second child paragraph text"
    }

    @Autowired
    lateinit var paragraphOperations: ParagraphOperationsService

    @Autowired
    lateinit var paragraphRepository: ParagraphRepository

    @Test
    fun insertParagraphBetween() {
        val p1 = Paragraph(documentId = DOCUMENT_ID, text = P1_TEXT)
        val p2 = Paragraph(documentId = DOCUMENT_ID, text = P2_TEXT)
        p1.next = p2
        var p1Id = paragraphRepository.save(p1).id
        var fetchedParagraph = paragraphRepository.findById(p1Id, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(P1_TEXT, fetchedParagraph.text)
        assertNotNull(fetchedParagraph.next)
        assertEquals(P2_TEXT, fetchedParagraph.next?.text)

        //insert in the middle
        paragraphOperations.insertParagraphBetween(
                documentId = DOCUMENT_ID,
                firstParagraphId = fetchedParagraph.id,
                secondParagraphId = fetchedParagraph.next?.id,
                paragraphText = MIDDLE_PARAGRAPH_TEXT)
        fetchedParagraph = paragraphRepository.findById(p1Id, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(P1_TEXT, fetchedParagraph.text)
        assertNotNull(fetchedParagraph.next)
        assertEquals(MIDDLE_PARAGRAPH_TEXT, fetchedParagraph.next?.text)
        assertNotNull(fetchedParagraph.next?.next)
        assertEquals(P2_TEXT, fetchedParagraph.next?.next?.text)
        assertNull(fetchedParagraph.next?.next?.next)

        //insert first
        p1Id = paragraphOperations.insertParagraphBetween(
                documentId = DOCUMENT_ID,
                secondParagraphId = fetchedParagraph.id,
                paragraphText = NEW_FIRST_PARAGRAPH_TEXT)
        fetchedParagraph = paragraphRepository.findById(p1Id, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(NEW_FIRST_PARAGRAPH_TEXT, fetchedParagraph.text)
        assertNotNull(fetchedParagraph.next)
        assertEquals(P1_TEXT, fetchedParagraph.next?.text)
        assertNotNull(fetchedParagraph.next?.next)
        assertEquals(MIDDLE_PARAGRAPH_TEXT, fetchedParagraph.next?.next?.text)

        //insert last
        paragraphOperations.insertParagraphBetween(
                documentId = DOCUMENT_ID,
                firstParagraphId = p2.id,
                paragraphText = NEW_LAST_PARAGRAPH_TEXT)
        fetchedParagraph = paragraphRepository.findById(p1Id, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(NEW_FIRST_PARAGRAPH_TEXT, fetchedParagraph.text)
        assertNotNull(fetchedParagraph.next)
        assertEquals(P1_TEXT, fetchedParagraph.next?.text)
        assertNotNull(fetchedParagraph.next?.next)
        assertEquals(MIDDLE_PARAGRAPH_TEXT, fetchedParagraph.next?.next?.text)
        assertNotNull(fetchedParagraph.next?.next?.next?.next)
        assertEquals(NEW_LAST_PARAGRAPH_TEXT, fetchedParagraph.next?.next?.next?.next?.text)
        assertNull(fetchedParagraph.next?.next?.next?.next?.next?.next)
    }

    @Test
    fun insertChildParagraph() {
        assertThat(paragraphRepository.findAll(-1), emptyIterable())
        val p1 = Paragraph(documentId = DOCUMENT_ID, text = P1_TEXT)
        val p2 = Paragraph(documentId = DOCUMENT_ID, text = P2_TEXT)
        p1.next = p2
        val p1Id = paragraphRepository.save(p1).id!!
        paragraphOperations.insertChildParagraph(DOCUMENT_ID, CHILD_PARAGRAPH_TEXT, p1Id)
        var fetchedParagraph = paragraphRepository.findById(p1Id, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(P1_TEXT, fetchedParagraph.text)
        assertNotNull(fetchedParagraph.child)
        assertEquals(CHILD_PARAGRAPH_TEXT, fetchedParagraph.child?.text)

        paragraphOperations.insertChildParagraph(DOCUMENT_ID, SECOND_CHILD_PARAGRAPH_TEXT, p1Id)
        fetchedParagraph = paragraphRepository.findById(p1Id, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(P1_TEXT, fetchedParagraph.text)
        assertNotNull(fetchedParagraph.child)
        assertEquals(SECOND_CHILD_PARAGRAPH_TEXT, fetchedParagraph.child?.text)
        assertNotNull(fetchedParagraph.child?.next)
        assertEquals(CHILD_PARAGRAPH_TEXT, fetchedParagraph.child?.next?.text)
    }

    @Test
    fun deleteParagraph() {
        val p1 = Paragraph(documentId = DOCUMENT_ID, text = P1_TEXT)
        val p2 = Paragraph(documentId = DOCUMENT_ID, text = P2_TEXT)
        val pMiddle = Paragraph(documentId = DOCUMENT_ID, text = MIDDLE_PARAGRAPH_TEXT)
        p1.next = pMiddle
        pMiddle.next = p2
        val p1Id = paragraphRepository.save(p1).id
        var fetchedParagraph = paragraphRepository.findById(p1Id, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(P1_TEXT, fetchedParagraph.text)
        assertNotNull(fetchedParagraph.next)
        assertEquals(MIDDLE_PARAGRAPH_TEXT, fetchedParagraph.next?.text)
        assertNotNull(fetchedParagraph.next?.next)
        assertEquals(P2_TEXT, fetchedParagraph.next?.next?.text)

        paragraphOperations.deleteParagraph(fetchedParagraph.next?.id!!)
        fetchedParagraph = paragraphRepository.findById(p1Id, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(P1_TEXT, fetchedParagraph.text)
        assertNotNull(fetchedParagraph.next)
        assertEquals(P2_TEXT, fetchedParagraph.next?.text)
    }

    @Test
    fun createParagraph() {
        assertThat(paragraphRepository.findAll(-1), emptyIterable())
        val createdId = paragraphOperations.createParagraph(documentId = DOCUMENT_ID, paragraphText = P1_TEXT)
        assertNotNull(createdId)
        val fetchedParagraph = paragraphRepository.findById(createdId, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(P1_TEXT, fetchedParagraph.text)
    }

    @Test
    fun updateParagraphText() {
        assertThat(paragraphRepository.findAll(-1), emptyIterable())
        val createdId = paragraphOperations.createParagraph(documentId = DOCUMENT_ID, paragraphText = P1_TEXT)
        assertNotNull(createdId)
        var fetchedParagraph = paragraphRepository.findById(createdId, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(P1_TEXT, fetchedParagraph.text)

        paragraphOperations.updateParagraphText(fetchedParagraph.id!!, P2_TEXT)
        fetchedParagraph = paragraphRepository.findById(createdId, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(P2_TEXT, fetchedParagraph.text)
    }

    @Test
    fun moveParagraphAfter() {
        assertThat(paragraphRepository.findAll(-1), emptyIterable())
        val p1 = Paragraph(documentId = DOCUMENT_ID, text = P1_TEXT)
        val p2 = Paragraph(documentId = DOCUMENT_ID, text = P2_TEXT)
        val pMiddle = Paragraph(documentId = DOCUMENT_ID, text = MIDDLE_PARAGRAPH_TEXT)
        p1.next = pMiddle
        pMiddle.next = p2
        val p1Id = paragraphRepository.save(p1).id
        var fetchedParagraph = paragraphRepository.findById(p1Id, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(P1_TEXT, fetchedParagraph.text)
        assertNotNull(fetchedParagraph.next)
        assertEquals(MIDDLE_PARAGRAPH_TEXT, fetchedParagraph.next?.text)
        assertNotNull(fetchedParagraph.next?.next)
        assertEquals(P2_TEXT, fetchedParagraph.next?.next?.text)
        assertNull(fetchedParagraph.next?.next?.next)

        paragraphOperations.moveParagraphAfter(fetchedParagraph.next?.id!!, fetchedParagraph.next?.next?.id!!)
        fetchedParagraph = paragraphRepository.findById(p1Id, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(P1_TEXT, fetchedParagraph.text)
        assertNotNull(fetchedParagraph.next)
        assertEquals(P2_TEXT, fetchedParagraph.next?.text)
        assertNotNull(fetchedParagraph.next?.next)
        assertEquals(MIDDLE_PARAGRAPH_TEXT, fetchedParagraph.next?.next?.text)
        assertNull(fetchedParagraph.next?.next?.next)
    }

    @Test
    fun moveParagraphBefore() {
        assertThat(paragraphRepository.findAll(-1), emptyIterable())
        val p1 = Paragraph(documentId = DOCUMENT_ID, text = P1_TEXT)
        val p2 = Paragraph(documentId = DOCUMENT_ID, text = P2_TEXT)
        val pMiddle = Paragraph(documentId = DOCUMENT_ID, text = MIDDLE_PARAGRAPH_TEXT)
        p1.next = pMiddle
        pMiddle.next = p2
        val p1Id = paragraphRepository.save(p1).id
        var fetchedParagraph = paragraphRepository.findById(p1Id, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(P1_TEXT, fetchedParagraph.text)
        assertNotNull(fetchedParagraph.next)
        assertEquals(MIDDLE_PARAGRAPH_TEXT, fetchedParagraph.next?.text)
        assertNotNull(fetchedParagraph.next?.next)
        assertEquals(P2_TEXT, fetchedParagraph.next?.next?.text)
        assertNull(fetchedParagraph.next?.next?.next)

        paragraphOperations.moveParagraphBefore(fetchedParagraph.next?.id!!, fetchedParagraph.id!!)
        fetchedParagraph = paragraphRepository.findById(pMiddle.id!!, -1).orElse(null)
        assertNotNull(fetchedParagraph)
        assertEquals(MIDDLE_PARAGRAPH_TEXT, fetchedParagraph.text)
        assertNotNull(fetchedParagraph.next)
        assertEquals(P1_TEXT, fetchedParagraph.next?.text)
        assertNotNull(fetchedParagraph.next?.next)
        assertEquals(P2_TEXT, fetchedParagraph.next?.next?.text)
        assertNull(fetchedParagraph.next?.next?.next)
    }
}