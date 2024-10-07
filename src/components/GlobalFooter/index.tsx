import React from 'react';
import './index.css';

export default function GlobalFooter() {
  const currentYear = new Date().getFullYear();
  return(
      <div className="global-footer">
        <div>© {currentYear} 龙腾智途 - 面试刷题平台</div>
        <div>
          <a href="https://gitee.com/ysds-kid" target="_blank">
            作者：程序员Lazy
          </a>
        </div>
      </div>
  );


}
