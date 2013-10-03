/*
 * (C) Copyright 2013 Kurento (http://kurento.org/)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package com.kurento.kmf.media;

import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.kurento.kmf.common.exception.KurentoMediaFrameworkException;
import com.kurento.kmf.media.internal.MediaPipelineImpl;
import com.kurento.kmf.media.internal.pool.MediaServerClientPoolService;
import com.kurento.kmf.media.internal.refs.MediaPipelineRef;
import com.kurento.kmf.media.params.MediaParam;
import com.kurento.kms.thrift.api.KmsMediaParam;
import com.kurento.kms.thrift.api.KmsMediaServerException;
import com.kurento.kms.thrift.api.KmsMediaServerService.AsyncClient;
import com.kurento.kms.thrift.api.KmsMediaServerService.AsyncClient.createMediaPipelineWithParams_call;
import com.kurento.kms.thrift.api.KmsMediaServerService.AsyncClient.createMediaPipeline_call;
import com.kurento.kms.thrift.api.KmsMediaServerService.Client;

public class MediaPipelineFactory {

	@Autowired
	private MediaServerClientPoolService clientPool;

	@Autowired
	private ApplicationContext ctx;

	public MediaPipeline create() {
		Client client = this.clientPool.acquireSync();

		MediaPipelineRef pipelineRefDTO;
		try {
			pipelineRefDTO = new MediaPipelineRef(
					client.createMediaPipeline());
		} catch (KmsMediaServerException e) {
			throw new KurentoMediaFrameworkException(e.getMessage(), e,
					e.getErrorCode());
		} catch (TException e) {
			// TODO change error code
			throw new KurentoMediaFrameworkException(e.getMessage(), e, 30000);
		} finally {
			this.clientPool.release(client);
		}

		MediaPipelineImpl pipeline = (MediaPipelineImpl) ctx.getBean(
				"mediaPipeline", pipelineRefDTO);
		return pipeline;
	}

	public MediaPipeline create(Map<String, MediaParam> params)
			throws KurentoMediaFrameworkException {

		Client client = this.clientPool.acquireSync();

		MediaPipelineRef pipelineRefDTO;
		try {
			// TODO Add real params map
			pipelineRefDTO = new MediaPipelineRef(
					client.createMediaPipelineWithParams(new HashMap<String, KmsMediaParam>()));
		} catch (KmsMediaServerException e) {
			throw new KurentoMediaFrameworkException(e.getMessage(), e,
					e.getErrorCode());
		} catch (TException e) {
			// TODO change error code
			throw new KurentoMediaFrameworkException(e.getMessage(), e, 30000);
		} finally {
			this.clientPool.release(client);
		}

		MediaPipelineImpl pipeline = (MediaPipelineImpl) ctx.getBean(
				"mediaPipeline", pipelineRefDTO);
		return pipeline;
	}

	public void create(final Continuation<MediaPipeline> cont)
			throws KurentoMediaFrameworkException {
		final AsyncClient client = this.clientPool.acquireAsync();

		try {
			client.createMediaPipeline(new AsyncMethodCallback<createMediaPipeline_call>() {

				@Override
				public void onError(Exception exception) {
					clientPool.release(client);
				}

				@Override
				public void onComplete(createMediaPipeline_call response) {
					MediaPipelineRef pipelineRefDTO;
					try {
						pipelineRefDTO = new MediaPipelineRef(response
								.getResult());
					} catch (KmsMediaServerException e) {
						throw new KurentoMediaFrameworkException(
								e.getMessage(), e, e.getErrorCode());
					} catch (TException e) {
						// TODO change error code
						throw new KurentoMediaFrameworkException(
								e.getMessage(), e, 30000);
					} finally {
						clientPool.release(client);
					}
					MediaPipelineImpl pipeline = (MediaPipelineImpl) ctx
							.getBean("mediaPipeline", pipelineRefDTO);
					cont.onSuccess(pipeline);
				}
			});
		} catch (TException e) {
			clientPool.release(client);
			// TODO change error code
			throw new KurentoMediaFrameworkException(e.getMessage(), e, 30000);
		}

	}

	public void create(Map<String, MediaParam> params,
			final Continuation<MediaPipeline> cont)
			throws KurentoMediaFrameworkException {

		final AsyncClient client = this.clientPool.acquireAsync();

		try {
			// TODO add real params
			client.createMediaPipelineWithParams(
					new HashMap<String, KmsMediaParam>(),
					new AsyncMethodCallback<createMediaPipelineWithParams_call>() {

						@Override
						public void onError(Exception exception) {
							clientPool.release(client);
						}

						@Override
						public void onComplete(
								createMediaPipelineWithParams_call response) {
							MediaPipelineRef pipelineRefDTO;
							try {
								pipelineRefDTO = new MediaPipelineRef(
										response.getResult());
							} catch (KmsMediaServerException e) {
								throw new KurentoMediaFrameworkException(e
										.getMessage(), e, e.getErrorCode());
							} catch (TException e) {
								// TODO change error code
								throw new KurentoMediaFrameworkException(e
										.getMessage(), e, 30000);
							} finally {
								clientPool.release(client);
							}
							MediaPipelineImpl pipeline = (MediaPipelineImpl) ctx
									.getBean("mediaPipeline", pipelineRefDTO);
							cont.onSuccess(pipeline);
						}
					});
		} catch (TException e) {
			clientPool.release(client);
			// TODO change error code
			throw new KurentoMediaFrameworkException(e.getMessage(), e, 30000);
		}

	}

}
