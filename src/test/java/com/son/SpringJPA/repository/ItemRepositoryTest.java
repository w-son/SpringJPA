package com.son.SpringJPA.repository;

import com.son.SpringJPA.domain.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void save() throws Exception {

        Item item = new Item("A");
        /* 요 과정에서 persist가 일어나면 JPA에서 id 값을 부여 해준다
         여기서 발생할 수 있는 문제
         Item의 id 값을 GeneratedValue로 생성하지 않고 직접 입력해서 객체를 생성하는 경우에 save를 통해서 저장을 한다면

         SimpleJpaRepository의 save에서 보는 것과 마찬가지로
         id 값을 확인한 후에 이 값이 null 이 아니면 persist가 아닌 merge를 실행한다
         그런데 merge는?? DB에 객체가 있음을 전제로 하고 여기에 select문을 호출해서 변경 내용을 합치는 작업을 한다
         = 즉, DB에 객체가 없으면 적용이 안된다는 의미이다
         결과적으로 새로 만든 인스턴스임에도 불구하고 save가 (정확히는 persist가) 적용이 안된다는 말이다

         -> Persistable 인터페이스와 createdDate Auditing 기법을 사용해서 이를 보완한다
         */
        itemRepository.save(item);

    }

}