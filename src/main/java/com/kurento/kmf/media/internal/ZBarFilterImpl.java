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
package com.kurento.kmf.media.internal;

import static com.kurento.kms.thrift.api.KmsMediaZBarFilterTypeConstants.EVENT_CODE_FOUND;
import static com.kurento.kms.thrift.api.KmsMediaZBarFilterTypeConstants.TYPE_NAME;

import com.kurento.kmf.media.Continuation;
import com.kurento.kmf.media.ListenerRegistration;
import com.kurento.kmf.media.ZBarFilter;
import com.kurento.kmf.media.events.CodeFoundEvent;
import com.kurento.kmf.media.events.MediaEventListener;
import com.kurento.kmf.media.internal.refs.MediaElementRef;

@ProvidesMediaElement(type = TYPE_NAME)
public class ZBarFilterImpl extends FilterImpl implements ZBarFilter {

	ZBarFilterImpl(MediaElementRef filterId) {
		super(filterId);
	}

	@Override
	public ListenerRegistration addCodeFoundDataListener(
			final MediaEventListener<CodeFoundEvent> vcaStrEvent) {
		return addListener(EVENT_CODE_FOUND, vcaStrEvent);
	}

	@Override
	public void addCodeFoundDataListener(
			final MediaEventListener<CodeFoundEvent> vcaStrEvent,
			final Continuation<ListenerRegistration> cont) {
		addListener(EVENT_CODE_FOUND, vcaStrEvent, cont);
	}

}
