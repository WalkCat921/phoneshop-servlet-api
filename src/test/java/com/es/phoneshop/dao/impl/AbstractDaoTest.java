package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.DAO;
import com.es.phoneshop.model.entity.Entity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractDaoTest<T extends Entity> {

    @Mock
    private T firstObj;
    @Mock
    private T secondObj;
    @Mock
    private T thirdObj;
    @Mock
    private T fourthObj;
    @Mock
    private T fifthObj;

    private static final Long FIRST = 1L;
    private static final Long SECOND = 2L;
    private static final Long THIRD = 3L;
    private static final Long FOURTH = 4L;

    @Spy
    private ArrayList<T> list;

    @InjectMocks
    private DAO<T> abstractDao = Mockito.spy(AbstractDao.class);

    @Before
    public void setup() {
        when(firstObj.getId()).thenReturn(FIRST);
        when(secondObj.getId()).thenReturn(SECOND);
        when(thirdObj.getId()).thenReturn(THIRD);
        when(fourthObj.getId()).thenReturn(FOURTH);
        list.addAll(List.of(firstObj, secondObj, thirdObj));
    }

    @After
    public void destroy() {
        list.clear();
    }

    @Test
    public void testFindAll() {
        assertEquals(List.of(firstObj, secondObj, thirdObj), abstractDao.findAll());
    }

    @Test
    public void testGetObjectById() {
        assertEquals(firstObj, abstractDao.get(FIRST));
    }

    @Test
    public void testSaveObject() {
        abstractDao.save(fourthObj);
        assertEquals(abstractDao.get(FOURTH), fourthObj);
    }

    @Test
    public void testDeleteObject() {
        final int countBeforeDelete = 3;
        final int countAfterDelete = 2;

        assertEquals(countBeforeDelete, abstractDao.findAll().size());
        abstractDao.delete(THIRD);

        assertEquals(countAfterDelete, abstractDao.findAll().size());
        assertEquals(List.of(firstObj, secondObj), list);
    }

    @Test
    public void testSaveWithoutIdObject() {
        when(fifthObj.getId()).thenReturn(null);
        abstractDao.save(fifthObj);
        assertEquals(List.of(firstObj, secondObj, thirdObj, fifthObj), list);
    }

    @Test
    public void testSaveWithSameIdObject() {
        abstractDao.save(firstObj);
        assertEquals(List.of(firstObj, secondObj, thirdObj), list);
    }
}