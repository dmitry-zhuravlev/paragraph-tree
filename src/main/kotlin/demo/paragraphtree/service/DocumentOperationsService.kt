package demo.paragraphtree.service

import demo.paragraphtree.model.Document
import demo.paragraphtree.repository.DocumentRepository
import demo.paragraphtree.repository.find
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Dmitry Zhuravlev
 *         Date:  06.12.2017
 */
@Service
@Transactional
class DocumentOperationsService {
    @Autowired
    lateinit var documentRepository: DocumentRepository

    fun getDocument(id: Long) = documentRepository.find(id)

    fun createDocument() = documentRepository.save(Document())

    fun updateDocument(document: Document) = documentRepository.save(document)

}