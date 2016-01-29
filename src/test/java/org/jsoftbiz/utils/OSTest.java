/*
 * Copyright 2014 Aurélien Broszniowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jsoftbiz.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Aurelien Broszniowski
 */

public class OSTest {

  @Test
  public void testReleaseFileWithLinuxPrettyName() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
    BufferedReader mockFile = mock(BufferedReader.class);
    when(mockFile.readLine()).thenReturn("NAME=Fedora", "PRETTY_NAME=\"Fedora 17 (Beefy Miracle)\"", "VERSION_ID=17", null);

    String name = "some name";
    String version = "4.1.4";
    String arch = "68000";
    OS.OsInfo osInfo = new OS().readPlatformName(name, version, arch, mockFile);
    Assert.assertThat(osInfo.getName(), is(equalTo(name)));
    Assert.assertThat(osInfo.getVersion(), is(equalTo(version)));
    Assert.assertThat(osInfo.getArch(), is(equalTo(arch)));
    Assert.assertThat(osInfo.getPlatformName(), is(equalTo("Fedora 17 (Beefy Miracle)")));
  }

  @Test
  public void testReleaseFileWithOneLine() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
    BufferedReader mockFile = mock(BufferedReader.class);
    String line = "Fedora version 19";
    when(mockFile.readLine()).thenReturn(line, null);

    String name = "some name";
    String version = "4.1.4";
    String arch = "68000";
    OS.OsInfo osInfo = new OS().readPlatformName(name, version, arch, mockFile);
    Assert.assertThat(osInfo.getName(), is(equalTo(name)));
    Assert.assertThat(osInfo.getVersion(), is(equalTo(version)));
    Assert.assertThat(osInfo.getArch(), is(equalTo(arch)));
    Assert.assertThat(osInfo.getPlatformName(), is(equalTo(line)));
  }

  @Test
  public void testReleaseFileWithTwoLines() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
    BufferedReader mockFile = mock(BufferedReader.class);
    when(mockFile.readLine()).thenReturn("Fedora version 19", "second line", null);

    String name = "some name";
    String version = "4.1.4";
    String arch = "68000";
    OS.OsInfo osInfo = new OS().readPlatformName(name, version, arch, mockFile);
    Assert.assertThat(osInfo.getName(), is(equalTo(name)));
    Assert.assertThat(osInfo.getVersion(), is(equalTo(version)));
    Assert.assertThat(osInfo.getArch(), is(equalTo(arch)));
    Assert.assertThat(osInfo.getPlatformName(), is(equalTo("Fedora version 19")));
  }

  @Test
  public void testLsbRelease() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
    BufferedReader mockFile = mock(BufferedReader.class);
    when(mockFile.readLine()).thenReturn("DISTRIB_ID=Ubuntu", "DISTRIB_RELEASE=9.10", "DISTRIB_CODENAME=karmic",
        "DISTRIB_DESCRIPTION=\"Ubuntu 9.10\"", null);

    String name = "some name";
    String version = "4.1.4";
    String arch = "68000";
    OS.OsInfo osInfo = new OS().readPlatformNameFromLsb(name, version, arch, mockFile);
    Assert.assertThat(osInfo.getName(), is(equalTo(name)));
    Assert.assertThat(osInfo.getVersion(), is(equalTo(version)));
    Assert.assertThat(osInfo.getArch(), is(equalTo(arch)));
    Assert.assertThat(osInfo.getPlatformName(), is(equalTo("Ubuntu 9.10 (karmic)")));
  }

  @Test
  public void testLinuxOsReleaseArchLinux() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
    BufferedReader mockFile = mock(BufferedReader.class);
    when(mockFile.readLine()).thenReturn(
            "LSB_VERSION=1.4",
            "DISTRIB_ID=Arch",
            "DISTRIB_RELEASE=rolling",
            "DISTRIB_DESCRIPTION=\"Arch Linux\"",
            null);

    String name = "some name";
    String version = "4.1.4";
    String arch = "68000";
    OS.OsInfo osInfo = new OS().readPlatformNameFromOsReleaseForArchLinux(name, version, arch, mockFile);
    Assert.assertThat(osInfo.getName(), is(equalTo(name)));
    Assert.assertThat(osInfo.getVersion(), is(equalTo(version)));
    Assert.assertThat(osInfo.getArch(), is(equalTo(arch)));
    Assert.assertThat(osInfo.getPlatformName(), is(equalTo("Arch Linux (Arch)")));
  }

  @Test
  public void testLinuxOsReleaseUbuntuLinux() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
    BufferedReader mockFile = mock(BufferedReader.class);
    when(mockFile.readLine()).thenReturn(
            "NAME=\"Ubuntu\"",
            "VERSION=\"14.04.3 LTS, Trusty Tahr\"",
            "ID=ubuntu",
            "ID_LIKE=debian",
            "PRETTY_NAME=\"Ubuntu 14.04.3 LTS\"",
            "VERSION_ID=\"14.04\"",
            "HOME_URL=\"http://www.ubuntu.com/\"",
            "SUPPORT_URL=\"http://help.ubuntu.com/\"",
            "BUG_REPORT_URL=\"http://bugs.launchpad.net/ubuntu/\"",
            null);

    String name = "some name";
    String version = "4.1.4";
    String arch = "68000";
    OS.OsInfo osInfo = new OS().readPlatformNameFromOsRelease(name, version, arch, mockFile);
    Assert.assertThat(osInfo.getName(), is(equalTo(name)));
    Assert.assertThat(osInfo.getVersion(), is(equalTo(version)));
    Assert.assertThat(osInfo.getArch(), is(equalTo(arch)));
    Assert.assertThat(osInfo.getPlatformName(), is(equalTo("Ubuntu 14.04.3 LTS, Trusty Tahr (ubuntu)")));
  }

  @Test
  public void testLinuxOsReleaseCentOs() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
    BufferedReader mockFile = mock(BufferedReader.class);
    when(mockFile.readLine()).thenReturn(
            "NAME=\"CentOS Linux\"",
            "VERSION=\"7 (Core)\"",
            "ID=\"centos\"",
            "ID_LIKE=\"rhel fedora\"",
            "VERSION_ID=\"7\"",
            "PRETTY_NAME=\"CentOS Linux 7 (Core)\"",
            "ANSI_COLOR=\"0;31\"",
            "CPE_NAME=\"cpe:/o:centos:centos:7\"",
            "HOME_URL=\"https://www.centos.org/\"",
            "BUG_REPORT_URL=\"https://bugs.centos.org/\"",
            "CENTOS_MANTISBT_PROJECT=\"CentOS-7\"",
            "CENTOS_MANTISBT_PROJECT_VERSION=\"7\"",
            "REDHAT_SUPPORT_PRODUCT=\"centos\"",
            "REDHAT_SUPPORT_PRODUCT_VERSION=\"7\"",
             null);

    String name = "some name";
    String version = "4.1.4";
    String arch = "68000";
    OS.OsInfo osInfo = new OS().readPlatformNameFromOsRelease(name, version, arch, mockFile);
    Assert.assertThat(osInfo.getName(), is(equalTo(name)));
    Assert.assertThat(osInfo.getVersion(), is(equalTo(version)));
    Assert.assertThat(osInfo.getArch(), is(equalTo(arch)));
    Assert.assertThat(osInfo.getPlatformName(), is(equalTo("CentOS Linux 7 (Core) (centos)")));
  }

  @Test
  public void testLinuxOsReleaseGentoo() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
    BufferedReader mockFile = mock(BufferedReader.class);
    when(mockFile.readLine()).thenReturn(
            "NAME=Gentoo",
            "ID=gentoo",
            "PRETTY_NAME=\"Gentoo/Linux\"",
            "ANSI_COLOR=\"1;32\"",
            "HOME_URL=\"http://www.gentoo.org/\"",
            "SUPPORT_URL=\"http://www.gentoo.org/main/en/support.xml\"",
            "BUG_REPORT_URL=\"https://bugs.gentoo.org/\"",
            null);

    String name = "some name";
    String version = "4.1.4";
    String arch = "68000";
    OS.OsInfo osInfo = new OS().readPlatformNameFromOsRelease(name, version, arch, mockFile);
    Assert.assertThat(osInfo.getName(), is(equalTo(name)));
    Assert.assertThat(osInfo.getVersion(), is(equalTo(version)));
    Assert.assertThat(osInfo.getArch(), is(equalTo(arch)));
    Assert.assertThat(osInfo.getPlatformName(), is(equalTo("Gentoo (gentoo)")));
  }

}
