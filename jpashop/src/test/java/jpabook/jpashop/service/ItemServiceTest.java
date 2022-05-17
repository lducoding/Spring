package jpabook.jpashop.service;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;
    @Autowired EntityManager em;

    @Test
    @Rollback(value = false)
    public void itemSave() throws Exception {
        //given
        Item album = new Album();
        album.setName("1집");
        album.setPrice(30000);
        album.setStockQuantity(10);
//        album.setArtist("sultan");
//        album.setEtc("명반");
        Category category = new Category();
        category.setName("album");

        //when
        itemService.itemSave(album);

        //then
        assertEquals(album, itemRepository.findOne(1L));
    }

    @Test
    public void findItem() throws Exception {
        //given

        //when
        List<Item> itemList = itemService.findItem();
        Item item1 = itemRepository.findOne(1L);
        System.out.println(item1.getId()+"11===================");
        Item item2 = itemRepository.findOne(2L);
        System.out.println(itemList);
        Item item3 = itemRepository.findOne(3L);

        //then
        assertEquals(item1, itemList.get(0));
        assertEquals(item1, itemList.get(1));
        assertEquals(item1, itemList.get(2));
    }

    @Test
    public void findOne() throws Exception {
        //given

        //when

        //then
    }

}