/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.gso.activator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openo.gso.commsvc.common.register.RegisterUtil;
import org.openo.gso.commsvc.common.util.JsonUtil;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.dao.impl.ServiceOperDaoImpl;
import org.openo.gso.dao.inf.IServiceModelDao;
import org.openo.gso.dao.inf.IServiceOperDao;
import org.openo.gso.dao.inf.IServiceSegmentDao;
import org.openo.gso.dao.multi.DatabaseSessionHandler;
import org.openo.gso.job.DeleteOperationJob;
import org.openo.gso.job.UpdateStatusJob;
import org.openo.gso.model.servicemo.ServiceModel;
import org.openo.gso.model.servicemo.ServiceOperation;
import org.openo.gso.model.servicemo.ServicePackageMapping;
import org.openo.gso.model.servicemo.ServiceParameter;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
import org.openo.gso.model.servicemo.ServiceSegmentOperation;
import org.openo.gso.util.service.SpringContextUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import mockit.Mock;
import mockit.MockUp;

/**
 * Test Activator.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/service.xml"})
public class ActivatorTest {

    /**
     * File path
     */
    private static final String FILE_PATH = "src/test/resources/json/";

    /**
     * SQL session.
     */
    SqlSession session = null;

    /**
     * Service DAO
     */
    IServiceModelDao svcModelDao = null;

    /**
     * Service operation DAO
     */
    IServiceOperDao svcOperDao = null;

    /**
     * Service segment DAO
     */
    IServiceSegmentDao segmentDao = null;

    /**
     * Before executing UT, start sql.<br/>
     * 
     * @since GSO 0.5
     */
    @Before
    public void start() throws IOException, SQLException {

        // Get database connect
        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        session = sqlSessionFactory.openSession();
        Connection conn = session.getConnection();

        ScriptRunner runner = new ScriptRunner(conn);

        // Run DB script
        reader = Resources.getResourceAsReader("ServiceModel.sql");
        runner.runScript(reader);

        reader = Resources.getResourceAsReader("ServicePackage.sql");
        runner.runScript(reader);

        reader = Resources.getResourceAsReader("ServiceSegment.sql");
        runner.runScript(reader);

        reader = Resources.getResourceAsReader("ServiceParameter.sql");
        runner.runScript(reader);

        reader = Resources.getResourceAsReader("inventory.sql");
        runner.runScript(reader);

        reader = Resources.getResourceAsReader("Operation.sql");
        runner.runScript(reader);

        reader.close();

        svcModelDao = (IServiceModelDao)SpringContextUtil.getBeanById("serviceModelDao");
        svcOperDao = (IServiceOperDao)SpringContextUtil.getBeanById("serviceOperDao");
        segmentDao = (IServiceSegmentDao)SpringContextUtil.getBeanById("serviceSegmentDao");

        mockSession();
    }

    /**
     * After executing UT, close session<br/>
     * 
     * @since GSO 0.5
     */
    @After
    public void stop() {
        session.close();
    }

    /**
     * Test update status job when service segments execution is failure.<br/>
     * 
     * @throws Exception when operation is failure.
     * @since GSO 0.5
     */
    @Test
    public void testUpdateStatusJobForError() throws Exception {
        testUpdateStatusJob("segmentOperationWithError.json", CommonConstant.Status.ERROR,
                CommonConstant.Progress.ONE_HUNDRED);
    }

    /**
     * Test update status job when service segments are finished.<br/>
     * 
     * @throws Exception when operation is failure.
     * @since GSO 0.5
     */
    @Test
    public void testUpdateStatusJobForFinished() throws Exception {
        testUpdateStatusJob("segmentOperationFinished.json", CommonConstant.Status.FINISHED,
                CommonConstant.Progress.ONE_HUNDRED);
    }

    /**
     * Test update status job when service segments are processing.<br/>
     * 
     * @throws Exception when operation is failure.
     * @since GSO 0.5
     */
    @Test
    public void testUpdateStatusJobForProcessing() throws Exception {
        testUpdateStatusJob("segmentOperationProcessing.json", CommonConstant.Status.PROCESSING, null);
    }

    /**
     * Test update status job when service has no service segments.<br/>
     * 
     * @throws Exception when operation is failure.
     * @since GSO 0.5
     */
    @Test
    public void testUpdateStatusJobWithoutSegs() throws Exception {
        testUpdateOperFinishTime("serviceWithoutSegOper.json", 1485400153344L, CommonConstant.Status.ERROR);
    }

    /**
     * Test update status job when operation of service with segment not update for 2h.<br/>
     * 
     * @throws Exception when operation is failure.
     * @since GSO 0.5
     */
    @Test
    public void testUpdateStatusJobForLongTime() throws Exception {
        testUpdateOperFinishTime("segmentOperationProcessing.json", System.currentTimeMillis(),
                CommonConstant.Status.PROCESSING);
    }

    /**
     * Test delete operation timing task.<br/>
     * 
     * @throws Exception when operation is failure.
     * @since GSO 0.5
     */
    @Test
    public void testDeleteOperationJob() throws Exception {

        // 1. prepare data
        insertSvcData();
        insertSegOper("segmentOperationFinished.json");
        List<ServiceModel> services = svcModelDao.queryServiceByStatus(CommonConstant.Status.PROCESSING);
        List<String> svcIds = new LinkedList<>();
        for(ServiceModel service : services) {
            svcIds.add(service.getServiceId());
        }
        List<ServiceOperation> svcOperations = svcOperDao.queryOperByIds(svcIds);
        mockHistoryOper(svcOperations);

        // 2. start test
        DeleteOperationJob delOper = new DeleteOperationJob();
        delOper.run();

        // 3. check test result
        Assert.assertTrue(CollectionUtils.isEmpty(svcOperDao.queryOperByIds(svcIds)));
        Assert.assertTrue(CollectionUtils.isEmpty(segmentDao.querySegmentOperByIds(svcIds)));
    }

    /**
     * Test update status job when finishing to delete service instance.<br/>
     * 
     * @throws Exception when operation is failure.
     * @since GSO 0.5
     */
    @Test
    public void testUpdateStatusJobForDelOper() throws Exception {

        // 1. construct finished result
        testUpdateStatusJob("segmentOperationFinished.json", CommonConstant.Status.FINISHED,
                CommonConstant.Progress.ONE_HUNDRED);

        // 2.1 delete old operation
        List<ServiceModel> services = svcModelDao.queryAllServices();
        List<String> svcIds = new LinkedList<>();
        for(ServiceModel service : services) {
            svcIds.add(service.getServiceId());
            svcModelDao.updateServiceStatus(service.getServiceId(), CommonConstant.Status.PROCESSING);
        }
        svcOperDao.deleteHistory(svcIds);

        // 2.2 insert new operation
        ServiceOperation svcOper = JsonUtil.unMarshal(getJsonString(FILE_PATH + "serviceOperationForDelOper.json"),
                ServiceOperation.class);
        svcOper.setServiceId("6cf110404cea452390bf996717527739");
        svcOperDao.insert(svcOper);

        // 3. test
        UpdateStatusJob updateJob = new UpdateStatusJob();
        updateJob.run();
    }

    /**
     * Test update status job.<br/>
     * 
     * @param file data json file
     * @param status service status
     * @param process service operation status
     * @since GSO 0.5
     */
    private void testUpdateStatusJob(String file, String status, String process) {
        insertSvcData();
        insertSegOper(file);
        UpdateStatusJob updateJob = new UpdateStatusJob();
        updateJob.run();

        // check service change
        List<ServiceModel> services = svcModelDao.queryServiceByStatus(status);
        Assert.assertNotNull(services);
        List<String> svcIds = new LinkedList<>();
        for(ServiceModel service : services) {
            svcIds.add(service.getServiceId());
        }

        // check service operation change
        List<ServiceOperation> operations = svcOperDao.queryOperByIds(svcIds);
        Assert.assertNotNull(operations);
        for(ServiceOperation oper : operations) {
            Assert.assertTrue(status.equals(oper.getResult()));
            if(null != process) {
                Assert.assertTrue(process.equals(Integer.toString(oper.getProgress())));
            } else {
                Assert.assertTrue((0 != oper.getProgress()) && (100 != oper.getProgress()));
            }
        }
    }

    /**
     * Prepare service data before testing.<br/>
     * 
     * @param segOperationPath file for segment operation data
     * @since GSO 0.5
     */
    private void insertSvcData() {

        // service instance
        ServiceModel svcModel = JsonUtil.unMarshal(getJsonString(FILE_PATH + "service.json"), ServiceModel.class);
        ServiceParameter param =
                JsonUtil.unMarshal(getJsonString(FILE_PATH + "serviceParameters.json"), ServiceParameter.class);
        svcModel.setParameter(param);
        ServicePackageMapping packageMapping =
                JsonUtil.unMarshal(getJsonString(FILE_PATH + "packageMapping.json"), ServicePackageMapping.class);
        svcModel.setServicePackage(packageMapping);
        // segmentNumber is set JsonIgnore, so need to add value when constructing data.
        svcModel.setSegmentNumber(2);
        svcModelDao.insert(svcModel);

        // service operation
        ServiceOperation svcOper =
                JsonUtil.unMarshal(getJsonString(FILE_PATH + "serviceOperation.json"), ServiceOperation.class);
        svcOper.setServiceId("6cf110404cea452390bf996717527739");
        svcOperDao.insert(svcOper);
    }

    /**
     * Prepare service segments before testing.<br/>
     * 
     * @since GSO 0.5
     */
    private void insertSegmens() {
        List<Object> segLst =
                (List)JsonUtil.unMarshal(getJsonString(FILE_PATH + "serviceSegments.json"), Map.class).get("segments");
        for(Object seg : segLst) {
            segmentDao.insertSegment(JsonUtil.unMarshal(JsonUtil.marshal(seg), ServiceSegmentModel.class));
        }
    }

    /**
     * Prepare segment operation data before testing.<br/>
     * 
     * @param segOperationPath file for segment operation data
     * @since GSO 0.5
     */
    private void insertSegOper(String segOperationPath) {
        List<Object> segOperLst =
                (List)JsonUtil.unMarshal(getJsonString(FILE_PATH + segOperationPath), Map.class).get("operations");
        for(Object segOper : segOperLst) {
            segmentDao.insertSegmentOper(JsonUtil.unMarshal(JsonUtil.marshal(segOper), ServiceSegmentOperation.class));
        }
    }

    /**
     * Get json string from file.<br/>
     * 
     * @param file the path of file
     * @return json string
     * @throws IOException when fail to read
     * @since GSO 0.5
     */
    private String getJsonString(final String file) {
        if(StringUtils.isEmpty(file)) {
            return "";
        }

        String json = null;
        try {
            FileInputStream fileStream = new FileInputStream(new File(file));
            json = IOUtils.toString(fileStream);
        } catch(Exception e) {
            Assert.fail(e.getMessage());
        }

        return json;
    }

    /**
     * Mock database session.<br/>
     * 
     * @since GSO 0.5
     */
    private void mockSession() {
        new MockUp<DatabaseSessionHandler>() {

            @Mock
            public SqlSession getSqlSession() {
                return session;
            }
        };
    }

    /**
     * Mock queryHistory method because h2 jdbc not support FROM_UNIXTIME.<br/>
     * 
     * @param operations prepared operation data.
     * @since GSO 0.5
     */
    private void mockHistoryOper(List<ServiceOperation> operations) {
        new MockUp<ServiceOperDaoImpl>() {

            @Mock
            public List<ServiceOperation> queryHistory() {
                return operations;
            }
        };
    }

    /**
     * test scene that service operation time is long.<br/>
     * 
     * @param file segment operation file
     * @param time service operation finish time
     * @since GSO 0.5
     */
    private void testUpdateOperFinishTime(String file, long time, String status) {
        insertSvcData();
        insertSegOper(file);
        UpdateStatusJob updateJob = new UpdateStatusJob();
        updateJob.run();

        List<ServiceModel> services = svcModelDao.queryServiceByStatus(status);
        if(status.equals(CommonConstant.Status.ERROR)) {
            Assert.assertFalse(CollectionUtils.isEmpty(services));
        }

        if(status.equals(CommonConstant.Status.PROCESSING)) {
            Assert.assertFalse(CollectionUtils.isEmpty(services));
        }
    }

    /**
     * Test start method.<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testStart() {
        new MockUp<RegisterUtil>() {

            @Mock
            public String readFile(String path) {
                return "test";
            }
        };
        ((Activator)SpringContextUtil.getBeanById("activatorBean")).start();
    }

    /**
     * Test stop method.<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testStop() {
        ((Activator)SpringContextUtil.getBeanById("activatorBean")).stop();
    }
}
