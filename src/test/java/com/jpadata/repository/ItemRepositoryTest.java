package com.jpadata.repository;

import com.jpadata.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;


@SpringBootTest
@Rollback(false)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void 직접아이디생성() throws Exception{
        //given
        Item item = new Item("item1","A");
        itemRepository.save(item);

        //when

        //then

    }

}
