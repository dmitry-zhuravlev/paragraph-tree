package demo.paragraphtree.config

import demo.paragraphtree.model.Document
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import org.neo4j.ogm.session.SessionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.io.File


/**
 * @author Dmitry Zhuravlev
 *         Date:  01.12.2017
 */
@Configuration
@EnableNeo4jRepositories("demo.paragraphtree.repository")
@EnableTransactionManagement
class Configuration{
    companion object {
        val file = File("paragraph.db")
    }

    @Bean(destroyMethod = "shutdown")
    fun graphDatabaseService() = GraphDatabaseFactory()
            .newEmbeddedDatabaseBuilder(file)
            .newGraphDatabase()

    @Bean
    fun getSessionFactory(): SessionFactory {
        return SessionFactory(Document::class.java.`package`.name)
    }

    @Bean
    fun transactionManager() = Neo4jTransactionManager(getSessionFactory())
}