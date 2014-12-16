/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sqoop.job.io;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.WritableComparable;
import org.apache.sqoop.connector.idf.IntermediateDataFormat;
import org.apache.sqoop.job.MRJobConstants;
import org.apache.sqoop.utils.ClassUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Writable used to load the data to the {@link #Transferable} entity TO
 */

public class SqoopWritable implements Configurable, WritableComparable<SqoopWritable> {
  private IntermediateDataFormat<?> toIDF;
  private Configuration conf;

  public SqoopWritable() {
    this(null);
  }

  public SqoopWritable(IntermediateDataFormat<?> dataFormat) {
    this.toIDF = dataFormat;
  }

  public void setString(String data) {
    this.toIDF.setCSVTextData(data);
  }

  public String getString() {
    return toIDF.getCSVTextData();
  }

  @Override
  public void write(DataOutput out) throws IOException {
    //delegate
    toIDF.write(out);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    //delegate
    toIDF.read(in);
  }

  @Override
  public int compareTo(SqoopWritable o) {
    return getString().compareTo(o.getString());
  }

  @Override
  public String toString() {
    return getString();
  }

  @Override
  public void setConf(Configuration conf) {
    this.conf = conf;

    if (toIDF == null) {
      String toIDFClass = conf.get(MRJobConstants.TO_INTERMEDIATE_DATA_FORMAT);
      this.toIDF = (IntermediateDataFormat<?>) ClassUtils.instantiate(toIDFClass);
    }
  }

  @Override
  public Configuration getConf() {
    return conf;
  }
}
