package jpabook.jpashop.service;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
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
        Item item = new Album();
        Book book = new Book();
        book.setName("책");
        book.setId(2L);
        book.setAuthor("하하");
        item.setName("술탄오브더디스코");
        item.setPrice(30000);
        item.setId(1L);
        item.setStockQuantity(30);
        itemService.itemSave(item);
        itemService.itemSave(book);
        //when
        List<Item> itemList = itemService.findItem();
        Item item1 = itemRepository.findOne(1L);
        System.out.println(itemList);

        //then
        assertEquals(item1, itemList.get(0));
    }

    @Test
    public void findOne() throws Exception {
        //given
        Item album = new Album();
        album.setName("1집");
        album.setPrice(30000);
        album.setStockQuantity(10);
        //when

        //then
    }

}