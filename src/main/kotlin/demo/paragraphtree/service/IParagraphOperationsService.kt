package demo.paragraphtree.service

import demo.paragraphtree.model.Paragraph

/**
 * @author Dmitry Zhuravlev
 *         Date:  08.12.2017
 */
interface IParagraphOperationsService {
    fun getParagraph(id: Long): Paragraph?
    fun updateParagraphText(paragraphId: Long, newParagraphText: String)
    fun moveParagraphBefore(paragraphId: Long, beforeParagraphId: Long)
    fun moveParagraphAfter(paragraphId: Long, afterParagraphId: Long)
    fun deleteParagraph(paragraphId: Long)
    fun createParagraph(documentId: Long, paragraphText: String): Long?
    fun insertChildParagraph(documentId: Long, paragraphText: String, parentParagraphId: Long): Long?
    fun insertParagraphBetween(documentId: Long, paragraphText: String, firstParagraphId: Long? = null, secondParagraphId: Long? = null): Long?
}