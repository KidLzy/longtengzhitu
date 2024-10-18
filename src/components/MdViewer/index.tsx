import { Viewer } from "@bytemd/react";
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import "bytemd/dist/index.css";
import "highlight.js/styles/vs.css";
import "./index.css";
import 'github-markdown-css/github-markdown-light.css';
import {setTheme, themeList} from "bytemd-plugin-theme";
import {useEffect, useState} from "react";
import {Select} from "antd";


interface Props {
  value?: string;
  theme?: string;
}


const plugins = [gfm(), highlight()];

/**
 * Markdown 浏览器
 * @param props
 * @constructor
 */
const MdViewer = (props: Props) => {
  const { value = "", theme = "channing-cyan" } = props;
    // const { value = ""} = props;

    useEffect(() => {
    setTheme(theme);
  }, [theme]);

  return (

      <div className="md-viewer">

          <Viewer value={value} plugins={plugins} />
      </div>
  );
};
export default MdViewer;
