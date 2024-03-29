/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.meta.ease.generator.mybatis.engine;

/**
 * Velocity 模板引擎实现文件输出
 *
 * @author hubin
 * @since 2018-01-10
 */
/*

public class VelocityTemplateEngine extends AbstractTemplateEngine {
 private VelocityEngine velocityEngine;

 {
     try {
         Class.forName("org.apache.velocity.util.DuckType");
     } catch (ClassNotFoundException e) {
         // velocity1.x的生成格式错乱 https://github.com/baomidou/generator/issues/5
         logger.warn("Velocity 1.x is outdated, please upgrade to 2.x or later.");
     }
 }

 @Override
 public @Nonnull
 VelocityTemplateEngine init(@Nonnull ConfigBuilder configBuilder) {
     if (null == velocityEngine) {
         Properties p = new Properties();
         p.setProperty(ConstVal.VM_LOAD_PATH_KEY, ConstVal.VM_LOAD_PATH_VALUE);
         p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, StringPool.EMPTY);
         p.setProperty(Velocity.ENCODING_DEFAULT, ConstVal.UTF8);
         p.setProperty(Velocity.INPUT_ENCODING, ConstVal.UTF8);
         p.setProperty("file.resource.loader.unicode", StringPool.TRUE);
         velocityEngine = new VelocityEngine(p);
     }
     return this;
 }


 @Override
 public void writer(@Nonnull Map<String, Object> objectMap, @Nonnull String templatePath, @Nonnull File outputFile) throws Exception {
     Template template = velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
     try (FileOutputStream fos = new FileOutputStream(outputFile);
          OutputStreamWriter ow = new OutputStreamWriter(fos, ConstVal.UTF8);
          BufferedWriter writer = new BufferedWriter(ow)) {
         template.merge(new VelocityContext(objectMap), writer);
     }
 }


 @Override
 public @Nonnull String templateFilePath(@Nonnull String filePath) {
     final String dotVm = ".vm";
     return filePath.endsWith(dotVm) ? filePath : filePath + dotVm;
 }
}
*/
