package demo.paragraphtree.service

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan

/**
 * @author Dmitry Zhuravlev
 *         Date:  04.12.2017
 */
@SpringBootConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
@ComponentScan("demo.paragraphtree.repository", "demo.paragraphtree.service", "demo.paragraphtree.config")
annotation class IntegrationTest