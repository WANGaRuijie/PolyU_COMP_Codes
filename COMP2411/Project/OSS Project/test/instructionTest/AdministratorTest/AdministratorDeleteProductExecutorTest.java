package instructionTest.AdministratorTest;

import database.Database;
import model.instruction.administrator.AdministratorDeleteProductExecutor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class AdministratorDeleteProductExecutorTest {

    private AdministratorDeleteProductExecutor executor;

    @Mock
    private Database mockedDatabase;

    @Mock
    private ResultSet mockedResultSet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        executor = new AdministratorDeleteProductExecutor("1234567890");
    }

    @Test
    public void testIsExistedProductID_ExistedProductID_ReturnsTrue() throws SQLException {
        when(mockedDatabase.contains("PRODUCT", "PRODUCT_ID", "123")).thenReturn(true);
        boolean isExisted = AdministratorDeleteProductExecutor.isExistedProductID("123");
        Assert.assertTrue(isExisted);
    }

    @Test
    public void testIsExistedProductID_NonExistentProductID_ReturnsFalse() throws SQLException {
        when(mockedDatabase.contains("PRODUCT", "PRODUCT_ID", "456")).thenReturn(false);
        boolean isExisted = AdministratorDeleteProductExecutor.isExistedProductID("456");
        Assert.assertFalse(isExisted);
    }

    @Test
    public void testGetProductInformation_ValidProductID_ReturnsResultSet() throws SQLException {
        when(mockedDatabase.query("SELECT * FROM PRODUCT WHERE PRODUCT_ID = '123'")).thenReturn(mockedResultSet);
        ResultSet result = AdministratorDeleteProductExecutor.getProductInformation("123");
        Assert.assertEquals(mockedResultSet, result);
    }

    @Test
    public void testShowResultSetInformation_NextResultSet_PrintsInformation() throws SQLException {
        ResultSetMetaData mockedMetaData = mock(ResultSetMetaData.class);
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);
        when(mockedResultSet.next()).thenReturn(true, false);
        when(mockedResultSet.getString(1)).thenReturn("ID1");
        when(mockedResultSet.getString(2)).thenReturn("Product1");

        AdministratorDeleteProductExecutor.showResultSetInformation(mockedResultSet);

        verify(mockedResultSet, times(2)).next();
        verify(mockedResultSet).getString(1);
        verify(mockedResultSet).getString(2);
        verify(mockedMetaData).getColumnCount();
    }

    @Test
    public void testShowResultSetInformation_NoResultSet_PrintsNoResult() throws SQLException {
        ResultSet emptyResultSet = mock(ResultSet.class);
        when(emptyResultSet.next()).thenReturn(false);

        AdministratorDeleteProductExecutor.showResultSetInformation(emptyResultSet);

        verify(emptyResultSet).next();
        System.out.println("No result!");
    }

    @Test
    public void testVisitDatabase_DeletesProductFromDatabase() throws SQLException {
        String sql = "DELETE FROM PRODUCT WHERE PRODUCT_ID = '123'";
        executor.visitDatabase();
        verify(mockedDatabase).delete(sql);
    }
}