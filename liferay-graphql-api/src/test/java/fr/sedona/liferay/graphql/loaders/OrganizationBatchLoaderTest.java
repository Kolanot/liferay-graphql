package fr.sedona.liferay.graphql.loaders;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Test suite for {@link OrganizationBatchLoader}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        DynamicQueryFactoryUtil.class,
        PropertyFactoryUtil.class
})
public class OrganizationBatchLoaderTest {

    @InjectMocks
    OrganizationBatchLoader batchLoader = new OrganizationBatchLoader();

    @Mock
    private OrganizationLocalService localService;

    @Before
    public void setUp() {
        DynamicQuery returnedQuery = mock(DynamicQuery.class);
        mockStatic(DynamicQueryFactoryUtil.class);
        when(DynamicQueryFactoryUtil.forClass(any()))
                .thenReturn(returnedQuery);

        Property returnedProperty = mock(Property.class);
        mockStatic(PropertyFactoryUtil.class);
        when(PropertyFactoryUtil.forName(anyString()))
                .thenReturn(returnedProperty);
    }

    @Test
    public void load_should_return_an_object_list() throws ExecutionException, InterruptedException {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<Object> expectedResults = new ArrayList<>();
        IntStream.rangeClosed(1, 3)
                .forEach(value -> {
                    Organization entity = mock(Organization.class);
                    entity.setOrganizationId(value);
                    expectedResults.add(entity);
                });

        // When / Then
        when(localService.dynamicQuery(any(DynamicQuery.class)))
                .thenReturn(expectedResults);

        // Asserts
        CompletionStage<List<Organization>> returnedResults = batchLoader.load(ids);
        assertNotNull(returnedResults);

        CompletableFuture<List<Organization>> asyncResults;
        if (returnedResults instanceof CompletableFuture) {
            asyncResults = (CompletableFuture<List<Organization>>) returnedResults;
        } else {
            asyncResults = returnedResults.toCompletableFuture();
        }
        assertNotNull(asyncResults);

        List<Organization> results = asyncResults.get();
        assertNotNull(results);
        assertEquals(expectedResults, results);
    }

    @Test
    public void load_with_empty_list_arg_should_return_an_empty_object_list() throws ExecutionException, InterruptedException {
        // Given
        List<Long> ids = Collections.emptyList();

        List<Object> expectedResults = Collections.emptyList();

        // When / Then
        when(localService.dynamicQuery(any(DynamicQuery.class)))
                .thenReturn(expectedResults);

        // Asserts
        CompletionStage<List<Organization>> returnedResults = batchLoader.load(ids);
        assertNotNull(returnedResults);

        CompletableFuture<List<Organization>> asyncResults;
        if (returnedResults instanceof CompletableFuture) {
            asyncResults = (CompletableFuture<List<Organization>>) returnedResults;
        } else {
            asyncResults = returnedResults.toCompletableFuture();
        }
        assertNotNull(asyncResults);

        List<Organization> results = asyncResults.get();
        assertNotNull(results);
        assertEquals(expectedResults, results);
    }

    @Test
    public void load_with_null_arg_should_return_an_empty_object_list() throws ExecutionException, InterruptedException {
        // Given
        List<Object> expectedResults = Collections.emptyList();

        // When / Then
        when(localService.dynamicQuery(any(DynamicQuery.class)))
                .thenReturn(expectedResults);

        // Asserts
        CompletionStage<List<Organization>> returnedResults = batchLoader.load(null);
        assertNotNull(returnedResults);

        CompletableFuture<List<Organization>> asyncResults;
        if (returnedResults instanceof CompletableFuture) {
            asyncResults = (CompletableFuture<List<Organization>>) returnedResults;
        } else {
            asyncResults = returnedResults.toCompletableFuture();
        }
        assertNotNull(asyncResults);

        List<Organization> results = asyncResults.get();
        assertNotNull(results);
        assertEquals(expectedResults, results);
    }
}
