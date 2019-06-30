package edu.pasudo123.app.todo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@Slf4j
public class TodoJpaTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testCreateTodo(){

        String content = "스프링 DATA JPA 공부하기";

        Todo todo = Todo.builder()
                .content(content)
                .build();

        Todo savedTodo = testEntityManager.persist(todo);

        // 동일한 값 반환
        assertThat(todo.getContent()).isEqualTo(savedTodo.getContent());

        log.debug("Todo Id : {}", savedTodo.getId());
        log.debug("Todo Content : {}", savedTodo.getContent());
        log.debug("Todo Create-Date : {}", savedTodo.getCreateDate());
        log.debug("Todo Update-Date : {}", savedTodo.getUpdateDate());
    }

    @Test
    public void testFindAllTodo(){

        String content1 = "스프링 시큐리티 공부하기";
        Todo todo1 = Todo.builder().content(content1).build();
        testEntityManager.persist(todo1);

        String content2 = "스프링 웹 MVC 공부하기";
        Todo todo2 = Todo.builder().content(content2).build();
        testEntityManager.persist(todo2);

        List<Todo> todoList = todoRepository.findAll();

        assertThat(todoList.size()).isEqualTo(2);

        for(Todo todoElement : todoList){
            log.debug("Todo Id : {}", todoElement.getId());
            log.debug("Todo Content : {}", todoElement.getContent());
            log.debug("Todo Create-Date : {}", todoElement.getCreateDate());
            log.debug("Todo Update-Date : {}", todoElement.getUpdateDate());
            log.debug("{}", "--");
        }
    }

    @Test
    public void testUpdateTodo(){

        /**
         * 레파지토리`s save() 시 영속성 컨텍스트에 해당 엔티티가 살아있다.
         * 이후 detach() 를 통해 영속성 컨텍스트의 엔티티를 준 영속성으로 전환
         */

        String content = "스프링 세션 공부하기";
        Todo todo = Todo.builder().content(content).build();
        Todo savedTodo = todoRepository.save(todo);
        testEntityManager.detach(savedTodo);
        testEntityManager.clear();

        Todo foundTodo = todoRepository.findById(1L).orElse(null);

        if(foundTodo == null){
            return;
        }

        String newContent = "스프링 세션 공부하지 않기";
        RequestTodoDto requestTodoDto = new RequestTodoDto();
        requestTodoDto.setContent(newContent);

        // 업데이트 수행
        foundTodo.updateContent(requestTodoDto.getContent());

        // 동일 ID 조회
        Todo updatedTodo = todoRepository.findById(1L).orElse(null);

        log.debug("Todo Id : {}", savedTodo.getId());
        log.debug("Todo Content : {}", savedTodo.getContent());
        log.debug("{}", "--");
        log.debug("Todo Id : {}", updatedTodo.getId());
        log.debug("Todo Content : {}", updatedTodo.getContent());

        // 테스트
        assertThat(savedTodo.getId()).isEqualTo(updatedTodo.getId());
        assertThat(savedTodo.getContent()).isNotEqualTo(updatedTodo.getContent());
    }
}
