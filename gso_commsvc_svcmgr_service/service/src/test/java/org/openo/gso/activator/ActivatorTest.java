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
import org.openo.gso.dao.inf.IServiceSegmentDao;
import org.openo.gso.dao.multi.DatabaseSessionHandler;
import org.openo.gso.job.UpdateStatusJob;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
import org.openo.gso.util.json.JsonUtil;
import org.openo.gso.util.service.SpringContextUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    SqlSession session;

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
     * Test update status job<br/>
     * 
     * @throws Exception when operation is failure.
     * @since GSO 0.5
     */
    @Test
    public void testUpdateStatusJob() throws Exception {
        String jsonString = getJsonString(FILE_PATH + "createServiceSegment.json");
        ServiceSegmentModel segment = JsonUtil.unMarshal(jsonString, ServiceSegmentModel.class);
        IServiceSegmentDao segmentDao = (IServiceSegmentDao)SpringContextUtil.getBeanById("serviceSegmentDao");
        segmentDao.insertSegment(segment);
        UpdateStatusJob updateJob = new UpdateStatusJob();
        updateJob.run();
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
}
