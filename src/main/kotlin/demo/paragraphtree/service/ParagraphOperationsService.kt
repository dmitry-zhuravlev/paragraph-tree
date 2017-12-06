package demo.paragraphtree.service

import demo.paragraphtree.model.Paragraph
import demo.paragraphtree.repository.ParagraphRepository
import demo.paragraphtree.repository.find
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Dmitry Zhuravlev
 *         Date:  04.12.2017
 */
@Service
@Transactional
class ParagraphOperationsService {

    @Autowired
    lateinit var paragraphRepository: ParagraphRepository

    fun getParagraph(id:Long) = paragraphRepository.find(id)

    fun updateParagraphText(paragraphId: Long, newParagraphText: String) {
        val currentParagraph = paragraphRepository.find(paragraphId) ?: return
        currentParagraph.text = newParagraphText
        paragraphRepository.save(currentParagraph)
    }

    fun moveParagraphBefore(paragraphId: Long, beforeParagraphId: Long) {
        val beforeParagraph = paragraphRepository.find(beforeParagraphId) ?: return
        val prevOfBeforeParagraph = paragraphRepository.findPreviousParagraph(beforeParagraph.id!!)
        if (prevOfBeforeParagraph != null) {
            moveParagraphAfter(paragraphId, prevOfBeforeParagraph.id!!)
        } else {
            val currentParagraph = paragraphRepository.find(paragraphId) ?: return
            val previousParagraph = paragraphRepository.findPreviousParagraph(paragraphId)
            if (previousParagraph != null) {
                previousParagraph.next = currentParagraph.next
            }
            currentParagraph.next = beforeParagraph
            paragraphRepository.save(currentParagraph)
        }
    }

    fun moveParagraphAfter(paragraphId: Long, afterParagraphId: Long) {
        val currentParagraph = paragraphRepository.find(paragraphId) ?: return
        val afterParagraph = paragraphRepository.find(afterParagraphId) ?: return
        val previousParagraph = paragraphRepository.findPreviousParagraph(paragraphId)
        if (previousParagraph != null) {
            previousParagraph.next = currentParagraph.next
        }
        currentParagraph.next = afterParagraph.next
        afterParagraph.next = currentParagraph
        if (previousParagraph != null) paragraphRepository.save(previousParagraph) else paragraphRepository.save(afterParagraph)
    }

    fun deleteParagraph(paragraphId: Long) {
        val currentParagraph = paragraphRepository.find(paragraphId) ?: return
        val previousParagraph = paragraphRepository.findPreviousParagraph(paragraphId)
        if (previousParagraph != null) {
            previousParagraph.next = currentParagraph.next
        }
        paragraphRepository.delete(currentParagraph)
    }

    fun createParagraph(documentId: Long, paragraphText: String) = insertParagraphBetween(documentId, paragraphText)

    fun insertChildParagraph(documentId: Long, paragraphText: String, parentParagraphId: Long): Long? {
        val newParagraph = Paragraph(documentId = documentId, text = paragraphText)
        val parentParagraph = paragraphRepository.find(parentParagraphId) ?: return null
        val existingChild = parentParagraph.child
        if (existingChild != null) {
            newParagraph.next = existingChild
            parentParagraph.child = newParagraph
        } else {
            parentParagraph.child = newParagraph
        }
        paragraphRepository.save(parentParagraph)
        return newParagraph.id
    }

    fun insertParagraphBetween(documentId: Long, paragraphText: String, firstParagraphId: Long? = null, secondParagraphId: Long? = null): Long? {
        val newParagraph = Paragraph(documentId = documentId, text = paragraphText)
        if (firstParagraphId == null && secondParagraphId == null) {
            paragraphRepository.save(newParagraph)
            return newParagraph.id
        }

        val insertBetween = firstParagraphId != null && secondParagraphId != null
        val insertFirst = firstParagraphId == null && secondParagraphId != null
        val insertLast = firstParagraphId != null && secondParagraphId == null

        when {
            insertFirst -> {
                val secondParagraph = paragraphRepository.find(secondParagraphId!!) ?: return null
                newParagraph.next = secondParagraph
                paragraphRepository.save(newParagraph)
            }
            insertLast -> {
                val firstParagraph = paragraphRepository.find(firstParagraphId!!) ?: return null
                firstParagraph.next = newParagraph
                paragraphRepository.save(firstParagraph)
            }
            insertBetween -> {
                val firstParagraph = paragraphRepository.find(firstParagraphId!!) ?: return null
                val secondParagraph = paragraphRepository.find(secondParagraphId!!) ?: return null
                firstParagraph.next = newParagraph
                newParagraph.next = secondParagraph
                paragraphRepository.save(firstParagraph)
            }
        }
        return newParagraph.id
    }
}