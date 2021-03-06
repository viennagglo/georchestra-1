/*
 * Copyright (C) 2009-2016 by the geOrchestra PSC
 *
 * This file is part of geOrchestra.
 *
 * geOrchestra is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * geOrchestra is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * geOrchestra.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.georchestra.analytics.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@NamedNativeQueries({

/*
 * Is seems that there is a bug in jpa implementation, results of count(*) queries are multiply by ten after five
 * requests one way to fix this issue is to explicitly cast count column to 'integer' type. This way, we always get
 * good results even after five requests.
 *
 * count(*) return a bigint type
 *
 * test:
 * @NamedNativeQuery(name="Stats.test",
 * query = "SELECT CAST(1 AS bigint) AS count"),
 * this will return 10 after 5 requests
 */

// no user / group filter
@NamedNativeQuery(name="Stats.getRequestCountBetweenStartDateAndEndDateByHour",
query = "SELECT CAST(COUNT(*) AS integer) AS count,	to_char(date, 'YYYY-mm-dd HH24') " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY to_char(date, 'YYYY-mm-dd HH24') " +
		"ORDER BY to_char(date, 'YYYY-mm-dd HH24')"),

@NamedNativeQuery(name="Stats.getRequestCountBetweenStartDateAndEndDateByDay",
query = "SELECT CAST(COUNT(*) AS integer) AS count,	to_char(date, 'YYYY-mm-dd') " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY to_char(date, 'YYYY-mm-dd') " +
		"ORDER BY to_char(date, 'YYYY-mm-dd')"),

@NamedNativeQuery(name="Stats.getRequestCountBetweenStartDateAndEndDateByWeek",
query = "SELECT CAST(COUNT(*) AS integer) AS count, to_char(date, 'YYYY-IW') " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY to_char(date, 'YYYY-IW') " +
		"ORDER BY to_char(date, 'YYYY-IW')"),

@NamedNativeQuery(name="Stats.getRequestCountBetweenStartDateAndEndDateByMonth",
query = "SELECT CAST(COUNT(*) AS integer) AS count,	to_char(date, 'YYYY-mm') " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY to_char(date, 'YYYY-mm') " +
		"ORDER BY to_char(date, 'YYYY-mm')"),

// users
@NamedNativeQuery(name="Stats.getRequestCountForUserBetweenStartDateAndEndDateByHour",
query = "SELECT CAST(COUNT(*) AS integer) AS count,	to_char(date, 'YYYY-mm-dd HH24') " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE user_name = :user " +
		"AND date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY to_char(date, 'YYYY-mm-dd HH24') " +
		"ORDER BY to_char(date, 'YYYY-mm-dd HH24')"),

@NamedNativeQuery(name="Stats.getRequestCountForUserBetweenStartDateAndEndDateByDay",
query = "SELECT CAST(COUNT(*) AS integer) AS count,	to_char(date, 'YYYY-mm-dd') " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE user_name = :user " +
		"AND date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY to_char(date, 'YYYY-mm-dd') " +
		"ORDER BY to_char(date, 'YYYY-mm-dd')"),

@NamedNativeQuery(name="Stats.getRequestCountForUserBetweenStartDateAndEndDateByWeek",
query = "SELECT CAST(COUNT(*) AS integer) AS count,	to_char(date, 'YYYY-IW') " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE user_name = :user " +
		"AND date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY to_char(date, 'YYYY-IW') " +
		"ORDER BY to_char(date, 'YYYY-IW')"),

@NamedNativeQuery(name="Stats.getRequestCountForUserBetweenStartDateAndEndDateByMonth",
query = "SELECT CAST(COUNT(*) AS integer) AS count,	to_char(date, 'YYYY-mm') " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE user_name = :user " +
		"AND date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY to_char(date, 'YYYY-mm') " +
		"ORDER BY to_char(date, 'YYYY-mm')"),

// groups
@NamedNativeQuery(name="Stats.getRequestCountForGroupBetweenStartDateAndEndDateByHour",
query = "SELECT CAST(COUNT(*) AS integer) AS count, to_char(date, 'YYYY-mm-dd HH24') " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE :group = ANY (roles) " +
		"AND date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY to_char(date, 'YYYY-mm-dd HH24') " +
		"ORDER BY to_char(date, 'YYYY-mm-dd HH24')"),

@NamedNativeQuery(name="Stats.getRequestCountForGroupBetweenStartDateAndEndDateByDay",
query = "SELECT CAST(COUNT(*) AS integer) AS count,	to_char(date, 'YYYY-mm-dd') " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE :group = ANY (roles) " +
		"AND date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY to_char(date, 'YYYY-mm-dd') " +
		"ORDER BY to_char(date, 'YYYY-mm-dd')"),

@NamedNativeQuery(name="Stats.getRequestCountForGroupBetweenStartDateAndEndDateByWeek",
query = "SELECT CAST(COUNT(*) AS integer) AS count,	to_char(date, 'YYYY-IW') " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE :group = ANY (roles) " +
		"AND date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY to_char(date, 'YYYY-IW') " +
		"ORDER BY to_char(date, 'YYYY-IW')"),

@NamedNativeQuery(name="Stats.getRequestCountForGroupBetweenStartDateAndEndDateByMonth",
query = "SELECT CAST(COUNT(*) AS integer) AS count, to_char(date, 'YYYY-mm') " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE :group = ANY (roles) " +
		"AND date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY to_char(date, 'YYYY-mm') " +
		"ORDER BY to_char(date, 'YYYY-mm')"),

// distinct users
@NamedNativeQuery(name="Stats.getDistinctUsersByGroup",
query = "SELECT user_name, org, CAST(COUNT(*) AS integer) AS count " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE :group = ANY (roles) " +
		"AND date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY user_name, org " +
		"ORDER BY COUNT(*) DESC"),

@NamedNativeQuery(name="Stats.getDistinctUsers",
query = "SELECT user_name, org, CAST(COUNT(*) AS integer) AS count " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"GROUP BY user_name, org " +
		"ORDER BY COUNT(*) DESC"),

// layer stats
@NamedNativeQuery(name="Stats.getLayersStatisticsForUser",
query = "SELECT layer, CAST(COUNT(*) AS integer) AS count " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"AND user_name = :user " +
		"AND layer != '' " +
		"GROUP BY layer " +
		"ORDER BY COUNT(*) DESC"),

@NamedNativeQuery(name="Stats.getLayersStatisticsForUserLimit",
query = "SELECT layer, CAST(COUNT(*) AS integer) AS count " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"AND user_name = :user " +
		"AND layer != '' " +
		"GROUP BY layer " +
		"ORDER BY COUNT(*) DESC " +
		"LIMIT :limit"),

@NamedNativeQuery(name="Stats.getLayersStatisticsForGroup",
query = "SELECT layer, CAST(COUNT(*) AS integer) AS count " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"AND :group = ANY(roles) " +
		"AND layer != '' " +
		"GROUP BY layer " +
		"ORDER BY COUNT(*) DESC"),

@NamedNativeQuery(name="Stats.getLayersStatisticsForGroupLimit",
query = "SELECT layer, CAST(COUNT(*) AS integer) AS count " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"AND :group = ANY(roles) " +
		"AND layer != '' " +
		"GROUP BY layer " +
		"ORDER BY COUNT(*) DESC " +
		"LIMIT :limit"),

@NamedNativeQuery(name="Stats.getLayersStatistics",
query = "SELECT layer, CAST(COUNT(*) AS integer) AS count " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"AND layer != '' " +
		"GROUP BY layer " +
		"ORDER BY COUNT(*) DESC"),

@NamedNativeQuery(name="Stats.getLayersStatisticsLimit",
query = "SELECT layer, CAST(COUNT(*) AS integer) AS count " +
		"FROM ogcstatistics.ogc_services_log " +
		"WHERE date >= CAST(:startDate AS timestamp without time zone) AND date < CAST(:endDate AS timestamp without time zone) " +
		"AND layer != '' " +
		"GROUP BY layer " +
		"ORDER BY COUNT(*) DESC " +
		"LIMIT :limit"),

// Extraction stats
@NamedNativeQuery(name="Stats.getLayersExtractionForUser",
		query = "SELECT layer_name, CAST(COUNT(*) AS integer) AS count " +
				"FROM extractorapp.extractor_layer_log " +
				"LEFT JOIN extractorapp.extractor_log " +
				"	ON (extractorapp.extractor_log.id = extractorapp.extractor_layer_log.extractor_log_id) " +
				"WHERE creation_date >= CAST(:startDate AS timestamp without time zone) AND creation_date < CAST(:endDate AS timestamp without time zone) " +
				"AND username = :user " +
				"AND is_successful " +
				"GROUP BY layer_name " +
				"ORDER BY COUNT(*) DESC"),

@NamedNativeQuery(name="Stats.getLayersExtractionForUserLimit",
		query = "SELECT layer_name, CAST(COUNT(*) AS integer) AS count " +
				"FROM extractorapp.extractor_layer_log " +
				"LEFT JOIN extractorapp.extractor_log " +
				"	ON (extractorapp.extractor_log.id = extractorapp.extractor_layer_log.extractor_log_id) " +
				"WHERE creation_date >= CAST(:startDate AS timestamp without time zone) AND creation_date < CAST(:endDate AS timestamp without time zone) " +
				"AND username = :user " +
				"AND is_successful " +
				"GROUP BY layer_name " +
				"ORDER BY COUNT(*) DESC " +
				"LIMIT :limit"),

@NamedNativeQuery(name="Stats.getLayersExtractionForGroup",
		query = "SELECT layer_name, CAST(COUNT(*) AS integer) AS count " +
				"FROM extractorapp.extractor_layer_log " +
				"LEFT JOIN extractorapp.extractor_log " +
				"	ON (extractorapp.extractor_log.id = extractorapp.extractor_layer_log.extractor_log_id) " +
				"WHERE creation_date >= CAST(:startDate AS timestamp without time zone) AND creation_date < CAST(:endDate AS timestamp without time zone) " +
				"AND is_successful " +
				"AND :group = ANY(roles) " +
				"GROUP BY layer_name " +
				"ORDER BY COUNT(*) DESC"),

@NamedNativeQuery(name="Stats.getLayersExtractionForGroupLimit",
		query = "SELECT layer_name, CAST(COUNT(*) AS integer) AS count " +
				"FROM extractorapp.extractor_layer_log " +
				"LEFT JOIN extractorapp.extractor_log " +
				"	ON (extractorapp.extractor_log.id = extractorapp.extractor_layer_log.extractor_log_id) " +
				"WHERE creation_date >= CAST(:startDate AS timestamp without time zone) AND creation_date < CAST(:endDate AS timestamp without time zone) " +
				"AND :group = ANY(roles) " +
				"AND is_successful " +
				"GROUP BY layer_name " +
				"ORDER BY COUNT(*) DESC " +
				"LIMIT :limit"),

@NamedNativeQuery(name="Stats.getLayersExtraction",
		query = "SELECT layer_name, CAST(COUNT(*) AS integer) AS count " +
				"FROM extractorapp.extractor_layer_log " +
				"LEFT JOIN extractorapp.extractor_log " +
				"	ON (extractorapp.extractor_log.id = extractorapp.extractor_layer_log.extractor_log_id) " +
				"WHERE creation_date >= CAST(:startDate AS timestamp without time zone) AND creation_date < CAST(:endDate AS timestamp without time zone) " +
				"AND is_successful " +
				"GROUP BY layer_name " +
				"ORDER BY COUNT(*) DESC"),

@NamedNativeQuery(name="Stats.getLayersExtractionLimit",
		query = "SELECT layer_name, CAST(COUNT(*) AS integer) AS count " +
				"FROM extractorapp.extractor_layer_log " +
				"LEFT JOIN extractorapp.extractor_log " +
				"	ON (extractorapp.extractor_log.id = extractorapp.extractor_layer_log.extractor_log_id) " +
				"WHERE creation_date >= CAST(:startDate AS timestamp without time zone) AND creation_date < CAST(:endDate AS timestamp without time zone) " +
				"AND is_successful " +
				"GROUP BY layer_name " +
				"ORDER BY COUNT(*) DESC " +
				"LIMIT :limit"),

@NamedNativeQuery(name="Stats.getFullLayersExtraction",
		query = "SELECT username, org, creation_date, CAST(duration AS text), creation_date + duration AS start_date, layer_name, is_successful, " +
				"trunc(CAST(ST_XMin(bbox) AS numeric), 5) || ',' || trunc(CAST(ST_YMin(bbox) AS numeric), 5) || ',' || trunc(CAST(ST_XMax(bbox) AS numeric), 5) || ',' || trunc(CAST(ST_YMax(bbox) AS numeric), 5) AS bbox, " +
				"ST_Area(CAST(bbox AS geography), TRUE) / 1000000 AS area_km2 " +
				"FROM extractorapp.extractor_layer_log " +
				"LEFT JOIN extractorapp.extractor_log " +
				"	ON (extractorapp.extractor_log.id = extractorapp.extractor_layer_log.extractor_log_id) " +
				"WHERE creation_date >= CAST(:startDate AS timestamp without time zone) AND creation_date < CAST(:endDate AS timestamp without time zone) "),
})
@Table(schema="ogcstatistics", name="ogc_services_log")
public class Stats {
	@Id
	@SequenceGenerator(name="ogc_services_log_id_seq", sequenceName="ogc_services_log_id_seq",
		schema="ogcstatistics", initialValue=1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ogc_services_log_id_seq")
	private long id;
	@Column(name="user_name")
	private String user;
	
	@Column(columnDefinition="Date")
	private Date date;
	private String service;
	private String layer;
	private String request;
	private String org;
	@Type(type = "org.georchestra.analytics.util.PostGresArrayStringType")
	private String[] roles;

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getService() {
		return service;
	}
	
	public void setService(String service) {
		this.service = service;
	}
	
	public String getLayer() {
		return layer;
	}
	
	public void setLayer(String layer) {
		this.layer = layer;
	}
	
	public String getRequest() {
		return request;
	}
	
	public void setRequest(String request) {
		this.request = request;
	}
	
	public String getOrg() {
		return org;
	}
	
	public void setOrg(String org) {
		this.org = org;
	}
	
	public String[] getRoles() {
		return roles;
	}
	
	public void setRoles(String[] roles) {
		this.roles = roles;
	}

}
