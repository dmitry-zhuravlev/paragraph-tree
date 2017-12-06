package demo.paragraphtree.repository

import demo.paragraphtree.model.Document
import org.springframework.data.neo4j.repository.Neo4jRepository

/**
 * @author Dmitry Zhuravlev
 *         Date:  01.12.2017
 */
interface DocumentRepository : Neo4jRepository<Document, Long>

fun DocumentRepository.find(id: Long, depth: Int = -1) = findById(id, depth).orElse(null)

